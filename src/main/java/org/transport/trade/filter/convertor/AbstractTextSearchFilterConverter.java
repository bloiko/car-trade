package org.transport.trade.filter.convertor;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.springframework.lang.NonNull;
import org.transport.trade.filter.AbstractFilter;
import org.transport.trade.filter.TextSearchFilter;

import static org.transport.trade.filter.convertor.ElasticSearchQueryBuilder.buildMatchQuery;

abstract class AbstractTextSearchFilterConverter implements FilterConverter {

    @Override
    public Query convert(@NonNull AbstractFilter filter) {
        TextSearchFilter textSearchFilter = (TextSearchFilter) filter;
        String text = textSearchFilter.getText();

        return buildMatchQuery(getEsFieldId(), text);
    }
}
