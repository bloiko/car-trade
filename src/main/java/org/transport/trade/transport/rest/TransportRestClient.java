package org.transport.trade.transport.rest;

import java.util.List;
import org.transport.trade.transport.dto.TransportDto;

public interface TransportRestClient {

    List<TransportDto> getTransports(int pageSize, int pageNum);
}
