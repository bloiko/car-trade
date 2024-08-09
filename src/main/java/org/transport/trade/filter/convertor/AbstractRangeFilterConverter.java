package org.transport.trade.filter.convertor;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import lombok.NonNull;
import org.transport.trade.filter.AbstractFilter;
import org.transport.trade.filter.RangeFilter;

abstract class AbstractRangeFilterConverter implements FilterConverter {

    @Override
    public Query convert(@NonNull AbstractFilter filter) {
        if (!(filter instanceof RangeFilter rangeFilter)) {
            throw new IllegalArgumentException(
                    "Expected RangeFilter but got " + filter.getClass().getSimpleName());
        }

        return buildRangeQuery(rangeFilter.getFrom(), rangeFilter.getTo());
    }

    private Query buildRangeQuery(String from, String to) {
        return RangeQuery.of(fn ->
                        fn.field(getEsFieldId()).from(from != null ? from : "").to(to != null ? to : ""))
                ._toQuery();
    }
}
