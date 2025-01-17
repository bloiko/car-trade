package org.transport.trade.elastic;

import co.elastic.clients.elasticsearch.core.SearchRequest;
import java.io.InputStream;
import java.util.List;
import org.transport.trade.transport.Transport;
import org.transport.trade.transport.dto.TransportsResponse;

public interface ElasticSearchTransportClient {

    Transport getById(String id);

    String index(Transport transport);

    void bulkIndex(List<? extends Transport> transports);

    void deleteById(String id);

    TransportsResponse search(SearchRequest searchRequest);

    List<String> getSuggestions(String textSearch, String fieldId);

    boolean healthCheck();

    void initializeMapping(InputStream inputStream);
}
