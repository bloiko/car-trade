package org.transport.trade.controller;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.transport.trade.dto.SearchRequest;
import org.transport.trade.dto.Sort;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

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

    private static SortOptions buildSortOptions(Sort sort) {
        return new SortOptions.Builder().field(f -> f.field(sort.getFieldName())
                                                     .order(SortOrder.valueOf(sort.getSortOrder().getValue()))).build();
    }

    private static List<Query> buildMustQueries(SearchRequest searchRequest) {
        Query transportTypeQuery = null;
        if (searchRequest.getTransportType() != null) {
            transportTypeQuery =
                    MatchQuery.of(m -> m.field("transportType").query(searchRequest.getTransportType().name()))
                              ._toQuery();
        }

        Query brandQuery = null;
        if (searchRequest.getBrand() != null) {
            brandQuery = MatchQuery.of(m -> m.field("brand").query(searchRequest.getBrand().name()))._toQuery();
        }

        return Stream.of(transportTypeQuery, brandQuery).filter(Objects::nonNull).toList();
    }
}
