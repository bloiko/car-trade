package org.transport.trade.controller.convertor;

import static org.transport.trade.controller.convertor.ElasticSearchQueryBuilder.buildMatchQuery;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.springframework.lang.NonNull;
import org.transport.trade.dto.filter.AbstractFilter;
import org.transport.trade.dto.filter.TextSearchFilter;

public abstract class AbstractTextSearchFilterConverter implements FilterConverter {

    @Override
    public Query convert(@NonNull AbstractFilter filter) {
        TextSearchFilter textSearchFilter = (TextSearchFilter) filter;
        String text = textSearchFilter.getText();

        return buildMatchQuery(getEsFieldId(), text);
    }
}
