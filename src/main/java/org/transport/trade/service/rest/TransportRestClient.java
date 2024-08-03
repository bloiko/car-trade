package org.transport.trade.service.rest;

import java.util.List;
import org.transport.trade.service.rest.dto.TransportDto;

public interface TransportRestClient {

    List<TransportDto> getTransports(int pageSize, int pageNum);
}
