package org.transport.trade.controller.convertor;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.transport.trade.dto.filter.Filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.apache.commons.lang3.Validate.notNull;
import static org.transport.trade.controller.convertor.ElasticSearchQueryBuilder.buildSortOptions;

@Component
public class FiltersConverter {

    private final Map<String, FilterConverter> filterConvertors;

    private final String indexName;

    public FiltersConverter(Map<String, FilterConverter> filterConvertors,
                            @Value("${elasticsearch.indexName}") String indexName) {
        this.filterConvertors = filterConvertors;
        this.indexName = indexName;
    }

    public SearchRequest convert(Filters filters) {
        List<Query> mustQueries = new ArrayList<>();
        filters.getFilters().forEach(filter -> {
            notNull(filter.getName(), "Filter name cannot be null");

            Optional.ofNullable(filterConvertors.get(filter.getName()))
                    .map(filterConverter -> filterConverter.convert(filter))
                    .ifPresent(mustQueries::add);
        });

        co.elastic.clients.elasticsearch.core.SearchRequest.Builder builder =
                new co.elastic.clients.elasticsearch.core.SearchRequest.Builder().index(indexName);

        if (isNotEmpty(mustQueries)) {
            builder.query(q -> q.bool(b -> b.must(mustQueries)));
        }

        Optional.ofNullable(filters.getSort()).ifPresent(sort -> builder.sort(buildSortOptions(sort)));

        return builder.size(Optional.ofNullable(filters.getPageSize()).orElse(10)).build();
    }
}
