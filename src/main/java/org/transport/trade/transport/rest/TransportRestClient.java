package org.transport.trade.transport.rest;

import org.transport.trade.transport.dto.TransportDto;

import java.util.List;

public interface TransportRestClient {

    List<TransportDto> getTransports(int pageSize, int pageNum);
}
