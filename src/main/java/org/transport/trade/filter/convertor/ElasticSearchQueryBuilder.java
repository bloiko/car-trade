package org.transport.trade.filter.convertor;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.transport.trade.filter.Sort;

public class ElasticSearchQueryBuilder {

    static Query buildMatchQuery(String fieldId, String value) {
        Query transportTypeQuery = null;
        if (value != null) {
            transportTypeQuery =
                    MatchQuery.of(m -> m.field(fieldId).query(value))._toQuery();
        }
        return transportTypeQuery;
    }

    static SortOptions buildSortOptions(Sort sort) {
        return new SortOptions.Builder()
                .field(f -> f.field(sort.getFieldName())
                        .order(SortOrder.valueOf(sort.getSortOrder().getValue())))
                .build();
    }
}
