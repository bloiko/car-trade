package org.transport.trade.controller.convertor.impl;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.transport.trade.controller.convertor.ElasticSearchQueryBuilder.buildMatchQuery;
import static org.transport.trade.controller.convertor.ElasticSearchQueryBuilder.buildSortOptions;

import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.transport.trade.dto.SearchRequest;

@Component
public class SearchRequestConverter {

    private final String indexName;

    public SearchRequestConverter(@Value("${elasticsearch.indexName}") String indexName) {
        this.indexName = indexName;
    }

    public co.elastic.clients.elasticsearch.core.SearchRequest buildSearchRequest(SearchRequest searchRequest) {
        co.elastic.clients.elasticsearch.core.SearchRequest.Builder builder =
                new co.elastic.clients.elasticsearch.core.SearchRequest.Builder().index(indexName);

        List<Query> mustQueries = buildMustQueries(searchRequest);
        if (isNotEmpty(mustQueries)) {
            builder.query(q -> q.bool(b -> b.must(mustQueries)));
        }

        Optional.ofNullable(searchRequest.getSort()).ifPresent(sort -> builder.sort(buildSortOptions(sort)));

        return builder.size(searchRequest.getPageSize()).build();
    }

    private static List<Query> buildMustQueries(SearchRequest searchRequest) {
        Query searchTextQuery = null;
        if (searchRequest.getSearchText() != null
                && !searchRequest.getSearchText().isEmpty()) {
            searchTextQuery = Query.of(q -> q.multiMatch(m -> m.query(searchRequest.getSearchText())
                    .fields("brand", "model")
                    .type(TextQueryType.CrossFields)
                    .operator(Operator.And)));
        }

        Query transportTypeQuery = buildMatchQuery("transportType", searchRequest.getTransportType());
        Query brandQuery = buildMatchQuery("brand", searchRequest.getBrand());
        Query modelQuery = buildMatchQuery("model", searchRequest.getModel());

        return Stream.of(searchTextQuery, transportTypeQuery, brandQuery, modelQuery)
                .filter(Objects::nonNull)
                .toList();
    }
}
