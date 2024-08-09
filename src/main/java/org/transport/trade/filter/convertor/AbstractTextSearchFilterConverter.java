package org.transport.trade.filter.convertor;

import static org.transport.trade.filter.convertor.ElasticSearchQueryBuilder.buildMatchQuery;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.springframework.lang.NonNull;
import org.transport.trade.filter.AbstractFilter;
import org.transport.trade.filter.TextSearchFilter;

abstract class AbstractTextSearchFilterConverter implements FilterConverter {

    @Override
    public Query convert(@NonNull AbstractFilter filter) {
        if (!(filter instanceof TextSearchFilter textSearchFilter)) {
            throw new IllegalArgumentException(
                    "Expected TextSearchFilter but got " + filter.getClass().getSimpleName());
        }

        return buildMatchQuery(getEsFieldId(), textSearchFilter.getText());
    }
}
