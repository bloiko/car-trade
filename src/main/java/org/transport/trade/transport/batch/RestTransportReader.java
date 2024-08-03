package org.transport.trade.transport.batch;

import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.List;
import org.springframework.batch.item.ItemReader;
import org.transport.trade.transport.dto.TransportDto;
import org.transport.trade.transport.rest.TransportRestClient;

public class RestTransportReader implements ItemReader<TransportDto> {

    private final TransportRestClient transportRestClient;
    private final int pageSize;
    private final int maxPageNum;
    private int currentPage = 1;
    private List<TransportDto> transportList;
    private int nextTransportIndex = 0;

    public RestTransportReader(TransportRestClient transportRestClient, int pageSize, int maxPageNum) {
        this.transportRestClient = transportRestClient;
        this.pageSize = pageSize;
        this.maxPageNum = maxPageNum;
    }

    @Override
    public TransportDto read() {
        if (currentPage > maxPageNum && nextTransportIndex >= transportList.size()) {
            return null;
        }

        if (transportList == null || nextTransportIndex >= transportList.size()) {
            transportList = transportRestClient.getTransports(pageSize, currentPage);
            currentPage++;
            nextTransportIndex = 0;
        }

        if (isEmpty(transportList)) {
            return null; // No more data
        }

        return transportList.get(nextTransportIndex++);
    }
}
