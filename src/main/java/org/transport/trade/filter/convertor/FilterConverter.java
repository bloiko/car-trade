package org.transport.trade.filter.convertor;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.springframework.lang.NonNull;
import org.transport.trade.filter.AbstractFilter;

interface FilterConverter {

    Query convert(@NonNull AbstractFilter filter);

    String getEsFieldId();
}
