package org.transport.trade.filter.convertor;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import lombok.NonNull;
import org.transport.trade.filter.AbstractFilter;
import org.transport.trade.filter.RangeFilter;

public abstract class AbstractRangeFilterConverter implements FilterConverter {

    @Override
    public Query convert(@NonNull AbstractFilter filter) {
        RangeFilter rangeFilter = (RangeFilter) filter;

        String from = rangeFilter.getFrom();
        String to = rangeFilter.getTo();

        return RangeQuery.of(fn -> fn.field(getEsFieldId()).from(from).to(to))._toQuery();
    }
}
