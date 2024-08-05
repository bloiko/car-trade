package org.transport.trade.elastic;

import co.elastic.clients.elasticsearch.core.SearchRequest;
import org.transport.trade.transport.Transport;
import org.transport.trade.transport.dto.TransportsResponse;

import java.util.List;

public interface ElasticSearchTransportClient {

    Transport getById(String id);

    String index(Transport transport);

    void deleteById(String id);

    TransportsResponse search(SearchRequest searchRequest);

    List<String> getSuggestions(String textSearch, String fieldId);
}