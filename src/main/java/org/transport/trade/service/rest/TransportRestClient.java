package org.transport.trade.service.rest;

import org.transport.trade.service.rest.dto.TransportDto;

import java.util.List;

public interface TransportRestClient {

    List<TransportDto> getTransports(int pageSize, int pageNum);
}
