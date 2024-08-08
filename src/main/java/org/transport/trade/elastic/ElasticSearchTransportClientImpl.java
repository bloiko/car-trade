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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.transport.trade.transport.Transport;
import org.transport.trade.transport.dto.TransportsResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
            TransportDocument transportDocument = elasticsearchClient
                    .get(s -> s.index(indexName).id(id), TransportDocument.class)
                    .source();
            return mapToTransport(transportDocument);
        } catch (IOException e) {
            throw new ElasticSearchOperationFailedException("Get by id failed", e);
        }
    }

    @Override
    public TransportsResponse search(SearchRequest searchRequest) {
        try {
            SearchResponse<TransportDocument> searchResponse =
                    elasticsearchClient.search(searchRequest, TransportDocument.class);

            return mapSearchResponse(searchResponse);
        } catch (IOException e) {
            throw new ElasticSearchOperationFailedException("Search failed", e);
        }
    }

    @Override
    public String index(Transport transport) {
        try {
            String generatedId = UUID.randomUUID().toString();
            transport.setId(generatedId);
            elasticsearchClient.index(
                    i -> i.index(indexName).id(generatedId).document(mapToTransportDocument(transport)));
            return generatedId;
        } catch (IOException e) {
            throw new ElasticSearchOperationFailedException("Index failed", e);
        }
    }

    @Override
    public void bulkIndex(List<? extends Transport> transports) {
        try {
            BulkRequest.Builder bulkRequestBuilder = new BulkRequest.Builder();
            for (Transport transport : transports) {
                String generatedId = UUID.randomUUID().toString();
                transport.setId(generatedId);
                bulkRequestBuilder.operations(BulkOperation.of(b -> b.index(IndexOperation.of(
                        i -> i.index(indexName).id(generatedId).document(mapToTransportDocument(transport))))));
            }
            BulkResponse bulkResponse = elasticsearchClient.bulk(bulkRequestBuilder.build());
            if (bulkResponse.errors()) {
                System.err.println("Bulk indexing had errors");
                bulkResponse.items().forEach(item -> {
                    if (item.error() != null) {
                        System.err.println(item.error().reason());
                    }
                });
            }
        } catch (IOException e) {
            throw new ElasticSearchOperationFailedException("Bulk index failed", e);
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
            SearchRequest request = SearchRequest.of(s -> s.index(indexName)
                    .suggest(sg -> sg.suggesters("suggestions", FieldSuggester.of(suggesterBuilder -> suggesterBuilder
                            .completion(completionBuilder -> completionBuilder
                                    .field(fieldId)
                                    .skipDuplicates(true)
                                    .size(5))
                            .text(textSearch)))));
            SearchResponse<Void> response = elasticsearchClient.search(request, Void.class);
            List<Suggestion<Void>> completionSuggestions = response.suggest().get("suggestions");
            if (completionSuggestions != null) {
                return completionSuggestions.stream()
                        .flatMap(suggestion -> suggestion.completion().options().stream())
                        .map(CompletionSuggestOption::text)
                        .collect(Collectors.toList());
            }
            return List.of();
        } catch (IOException e) {
            throw new ElasticSearchOperationFailedException("Failed to fetch suggestions", e);
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
        try {
            boolean indexExists = elasticsearchClient.indices().exists(e -> e.index(indexName)).value();
            if (!indexExists) {
                CreateIndexRequest createIndexRequest =
                        CreateIndexRequest.of(b -> b.index(indexName).mappings(m -> m.withJson(inputStream)));

                CreateIndexResponse createIndexResponse = elasticsearchClient.indices().create(createIndexRequest);

                if (!createIndexResponse.acknowledged()) {
                    throw new RuntimeException("Failed to create index: " + indexName);
                }
            }
        } catch (IOException e) {
            throw new ElasticSearchOperationFailedException("Failed to initialize mappings", e);
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
