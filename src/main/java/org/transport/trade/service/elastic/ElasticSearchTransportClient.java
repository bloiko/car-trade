package org.transport.trade.service.elastic;

import co.elastic.clients.elasticsearch.core.SearchRequest;
import java.util.List;
import org.transport.trade.dto.TransportsResponse;
import org.transport.trade.entity.Transport;

public interface ElasticSearchTransportClient {

    Transport getById(String id);

    void index(Transport transport);

    void deleteById(String id);

    TransportsResponse search(SearchRequest searchRequest);

    List<String> getSuggestions(String textSearch, String fieldId);
}
