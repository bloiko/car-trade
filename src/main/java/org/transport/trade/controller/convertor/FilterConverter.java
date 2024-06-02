package org.transport.trade.controller.convertor;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.springframework.lang.NonNull;
import org.transport.trade.dto.filter.AbstractFilter;

public interface FilterConverter {

    Query convert(@NonNull AbstractFilter filter);

    String getEsFieldId();
}
