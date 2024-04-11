package org.transport.trade.service.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.core.search.ResponseBody;
import org.springframework.stereotype.Service;
import org.transport.trade.dto.TransportsResponse;
import org.transport.trade.entity.Transport;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class ElasticSearchTransportClientImpl implements ElasticSearchTransportClient {

    // TODO as env variable
    public static final String INDEX_NAME = "car-trade";

    public static final String OPERATION_FAILED_IN_ELASTICSEARCH = "Operation failed in Elasticsearch";

    private final ElasticsearchClient elasticsearchClient;

    public ElasticSearchTransportClientImpl(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public Transport getById(String id) {
        try {
            return elasticsearchClient.get(s -> s.index(INDEX_NAME).id(id), Transport.class).source();
        } catch (IOException e) {
            throw new RuntimeException(OPERATION_FAILED_IN_ELASTICSEARCH, e); // TODO throw custom exception
        }
    }

    @Override
    public TransportsResponse search(SearchRequest searchRequest) {
        try {
            SearchResponse<Transport> searchResponse = elasticsearchClient.search(searchRequest, Transport.class);

            return mapSearchResponse(searchResponse);
        } catch (IOException e) {
            throw new RuntimeException(OPERATION_FAILED_IN_ELASTICSEARCH, e); // TODO throw custom exception
        }
    }

    @Override
    public void index(Transport transport) {
        try {
            String generatedId = UUID.randomUUID().toString();
            transport.setId(generatedId);
            elasticsearchClient.index(i -> i.index(INDEX_NAME).id(generatedId).document(transport));
        } catch (IOException e) {
            throw new RuntimeException(OPERATION_FAILED_IN_ELASTICSEARCH, e); // TODO throw custom exception
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            elasticsearchClient.delete(i -> i.index(INDEX_NAME).id(id));
        } catch (IOException e) {
            throw new RuntimeException(OPERATION_FAILED_IN_ELASTICSEARCH, e); // TODO throw custom exception
        }
    }

    private static TransportsResponse mapSearchResponse(SearchResponse<Transport> searchResponse) {
        TransportsResponse transportsResponse = new TransportsResponse();
        Optional<HitsMetadata<Transport>> hitsMetadata = Optional.ofNullable(searchResponse).map(ResponseBody::hits);

        hitsMetadata.ifPresent(hits -> {
            transportsResponse.setTransports(hits.hits().stream().map(Hit::source).toList());
        });
        hitsMetadata.map(HitsMetadata::total).ifPresent(totalHits -> transportsResponse.setTotal(totalHits.value()));

        return transportsResponse;
    }
}
