package org.transport.trade.service.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.transport.trade.dto.TransportsResponse;
import org.transport.trade.entity.Transport;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ElasticSearchTransportClientImpl implements ElasticSearchTransportClient {

    private final String indexName;

    private final ElasticsearchClient elasticsearchClient;

    public ElasticSearchTransportClientImpl(@Value("${elasticsearch.indexName}") String indexName, ElasticsearchClient elasticsearchClient) {
        this.indexName = indexName;
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public Transport getById(String id) {
        try {
            return elasticsearchClient.get(s -> s.index(indexName).id(id), Transport.class).source();
        } catch (IOException e) {
            throw new ElasticSearchOperationFailedException("Get by id failed", e);
        }
    }

    @Override
    public TransportsResponse search(SearchRequest searchRequest) {
        try {
            SearchResponse<Transport> searchResponse = elasticsearchClient.search(searchRequest, Transport.class);

            return mapSearchResponse(searchResponse);
        } catch (IOException e) {
            throw new ElasticSearchOperationFailedException("Search failed", e);
        }
    }

    @Override
    public void index(Transport transport) {
        try {
            String generatedId = UUID.randomUUID().toString();
            transport.setId(generatedId);
            elasticsearchClient.index(i -> i.index(indexName).id(generatedId).document(transport));
        } catch (IOException e) {
            throw new ElasticSearchOperationFailedException("Index failed", e);
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
                                                           .suggest(sg -> sg.suggesters("suggestions", FieldSuggester.of(suggesterBuilder -> suggesterBuilder.completion(completionBuilder -> completionBuilder.field(fieldId)
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

    private static TransportsResponse mapSearchResponse(SearchResponse<Transport> searchResponse) {
        TransportsResponse transportsResponse = new TransportsResponse();
        Optional<HitsMetadata<Transport>> hitsMetadata = Optional.ofNullable(searchResponse).map(ResponseBody::hits);

        hitsMetadata.ifPresent(hits -> transportsResponse.setTransports(hits.hits()
                                                                            .stream()
                                                                            .map(Hit::source)
                                                                            .toList()));
        hitsMetadata.map(HitsMetadata::total).ifPresent(totalHits -> transportsResponse.setTotal(totalHits.value()));

        return transportsResponse;
    }
}
