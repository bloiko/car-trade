package org.transport.trade.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.HealthStatus;
import co.elastic.clients.elasticsearch.cluster.HealthResponse;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation;
import co.elastic.clients.elasticsearch.core.search.*;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.transport.trade.transport.Transport;
import org.transport.trade.transport.dto.TransportsResponse;

@Service
public class ElasticSearchTransportClientImpl implements ElasticSearchTransportClient {

    private final String indexName;

    private final ElasticsearchClient elasticsearchClient;

    public ElasticSearchTransportClientImpl(
            @Value("${elasticsearch.indexName}") String indexName, ElasticsearchClient elasticsearchClient) {
        this.indexName = indexName;
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public Transport getById(String id) {
        try {
            return mapToTransport(elasticsearchClient
                    .get(s -> s.index(indexName).id(id), TransportDocument.class)
                    .source());
        } catch (IOException e) {
            throw new ElasticSearchOperationFailedException("Get by id failed", e);
        }
    }

    @Override
    public TransportsResponse search(SearchRequest searchRequest) {
        try {
            return mapSearchResponse(elasticsearchClient.search(searchRequest, TransportDocument.class));
        } catch (IOException e) {
            throw new ElasticSearchOperationFailedException("Search failed", e);
        }
    }

    @Override
    public String index(Transport transport) {
        try {
            String id = getOrGenerateId(transport);
            transport.setId(id);
            elasticsearchClient.index(i -> i.index(indexName).id(id).document(mapToTransportDocument(transport)));
            return id;
        } catch (IOException e) {
            throw new ElasticSearchOperationFailedException("Index failed", e);
        }
    }

    private static String getOrGenerateId(Transport transport) {
        return StringUtils.defaultIfBlank(transport.getId(), UUID.randomUUID().toString());
    }

    @Override
    public void bulkIndex(List<? extends Transport> transports) {
        try {
            BulkRequest bulkRequest = buildBulkRequest(transports);
            handleBulkResponse(elasticsearchClient.bulk(bulkRequest));
        } catch (IOException e) {
            throw new ElasticSearchOperationFailedException("Bulk index operation failed", e);
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            elasticsearchClient.delete(i -> i.index(indexName).id(id));
        } catch (IOException e) {
            throw new ElasticSearchOperationFailedException("Delete by id failed", e);
        }
    }

    @Override
    public List<String> getSuggestions(String textSearch, String fieldId) {
        try {
            SearchRequest request = buildSuggestionRequest(textSearch, fieldId);
            return fetchSuggestions(elasticsearchClient.search(request, Void.class));
        } catch (IOException e) {
            throw new ElasticSearchOperationFailedException("Failed to fetch suggestions for text: " + textSearch, e);
        }
    }

    @Override
    public boolean healthCheck() {
        try {
            HealthResponse healthResponse = elasticsearchClient.cluster().health();
            return healthResponse.status() != HealthStatus.Red;
        } catch (IOException e) {
            throw new ElasticSearchOperationFailedException("Health check failed", e);
        }
    }

    @Override
    public void initializeMapping(InputStream inputStream) {
        try (inputStream) {
            if (!isIndexExists()) {
                createIndex(inputStream);
            }
        } catch (IOException e) {
            throw new ElasticSearchOperationFailedException("Failed to initialize mappings", e);
        }
    }

    private SearchRequest buildSuggestionRequest(String textSearch, String fieldId) {
        return SearchRequest.of(s -> s.index(indexName)
                .suggest(sg -> sg.suggesters("suggestions", FieldSuggester.of(suggesterBuilder -> suggesterBuilder
                        .completion(completionBuilder -> completionBuilder
                                .field(fieldId)
                                .skipDuplicates(true)
                                .size(5))
                        .text(textSearch)))));
    }

    private List<String> fetchSuggestions(SearchResponse<Void> response) {
        List<Suggestion<Void>> completionSuggestions = response.suggest().get("suggestions");
        return completionSuggestions == null
                ? List.of()
                : completionSuggestions.stream()
                        .flatMap(suggestion -> suggestion.completion().options().stream())
                        .map(CompletionSuggestOption::text)
                        .collect(Collectors.toList());
    }

    private BulkRequest buildBulkRequest(List<? extends Transport> transports) {
        BulkRequest.Builder bulkRequestBuilder = new BulkRequest.Builder();
        transports.forEach(transport -> {
            transport.setId(getOrGenerateId(transport));
            bulkRequestBuilder.operations(BulkOperation.of(b -> b.index(IndexOperation.of(
                    i -> i.index(indexName).id(transport.getId()).document(mapToTransportDocument(transport))))));
        });
        return bulkRequestBuilder.build();
    }

    private void handleBulkResponse(BulkResponse bulkResponse) {
        if (bulkResponse.errors()) {
            bulkResponse.items().stream()
                    .filter(item -> item.error() != null)
                    .forEach(item -> System.err.println(item.error().reason()));
            throw new ElasticSearchOperationFailedException("Bulk indexing encountered errors");
        }
    }

    private boolean isIndexExists() throws IOException {
        return elasticsearchClient.indices().exists(e -> e.index(indexName)).value();
    }

    private void createIndex(InputStream inputStream) throws IOException {
        CreateIndexRequest createIndexRequest =
                CreateIndexRequest.of(b -> b.index(indexName).mappings(m -> m.withJson(inputStream)));
        CreateIndexResponse createIndexResponse = elasticsearchClient.indices().create(createIndexRequest);

        if (!createIndexResponse.acknowledged()) {
            throw new ElasticSearchOperationFailedException("Failed to create index: " + indexName);
        }
    }

    private static TransportsResponse mapSearchResponse(SearchResponse<TransportDocument> searchResponse) {
        TransportsResponse transportsResponse = new TransportsResponse();
        Optional<HitsMetadata<TransportDocument>> hitsMetadata =
                Optional.ofNullable(searchResponse).map(ResponseBody::hits);

        hitsMetadata.ifPresent(hits -> transportsResponse.setTransports(hits.hits().stream()
                .map(Hit::source)
                .filter(Objects::nonNull)
                .map(ElasticSearchTransportClientImpl::mapToTransport)
                .toList()));
        hitsMetadata.map(HitsMetadata::total).ifPresent(totalHits -> transportsResponse.setTotal(totalHits.value()));

        return transportsResponse;
    }

    private static Transport mapToTransport(TransportDocument transportDocument) {
        if (transportDocument == null) {
            return null;
        }
        Transport transport = new Transport();
        transport.setId(transportDocument.getId());
        transport.setTransportType(transportDocument.getTransportType());
        transport.setBrand(transportDocument.getBrand());
        transport.setModel(transportDocument.getModel());
        transport.setBodyType(transportDocument.getBodyType());
        transport.setPrice(transportDocument.getPrice());
        transport.setRegion(transportDocument.getRegion());
        transport.setManufacturerCountry(transportDocument.getManufacturerCountry());
        transport.setManufacturerYear(transportDocument.getManufacturerYear());
        return transport;
    }

    private static TransportDocument mapToTransportDocument(Transport transport) {
        if (transport == null) {
            return null;
        }
        TransportDocument transportDocument = new TransportDocument();
        transportDocument.setId(transport.getId());
        transportDocument.setTransportType(transport.getTransportType());
        transportDocument.setBrand(transport.getBrand());
        transportDocument.setModel(transport.getModel());
        transportDocument.setBodyType(transport.getBodyType());
        transportDocument.setPrice(transport.getPrice());
        transportDocument.setRegion(transport.getRegion());
        transportDocument.setRegionSuggest(new RegionSuggest(transport.getRegion()));
        transportDocument.setManufacturerCountry(transport.getManufacturerCountry());
        transportDocument.setManufacturerYear(transport.getManufacturerYear());
        return transportDocument;
    }
}
