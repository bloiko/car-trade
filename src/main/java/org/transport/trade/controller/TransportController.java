package org.transport.trade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.transport.trade.dto.SearchRequest;
import org.transport.trade.dto.TransportsResponse;
import org.transport.trade.entity.Transport;
import org.transport.trade.service.elastic.ElasticSearchTransportClient;

import static org.apache.commons.lang3.Validate.notNull;

@RestController
@RequestMapping("/transports")
public class TransportController {

    private final ElasticSearchTransportClient elasticSearchTransportClient;

    private final SearchRequestConverter searchRequestConverter;

    @Autowired
    public TransportController(ElasticSearchTransportClient elasticSearchTransportClient, SearchRequestConverter searchRequestConverter) {
        this.elasticSearchTransportClient = elasticSearchTransportClient;
        this.searchRequestConverter = searchRequestConverter;
    }

    @GetMapping("/transport/{transportId}")
    public Transport getTransportById(@PathVariable String transportId) {
        notNull(transportId, "transportId cannot be null");

        return elasticSearchTransportClient.getById(transportId);
    }

    @PostMapping("/transport")
    public void addTransport(@RequestBody Transport transport) {
        notNull(transport, "Request cannot be null");

        elasticSearchTransportClient.index(transport);
    }

    @DeleteMapping("/transport/{transportId}")
    public void deleteTransport(@PathVariable String transportId) {
        notNull(transportId, "transportId cannot be null");

        elasticSearchTransportClient.deleteById(transportId);
    }

    @PostMapping
    public TransportsResponse searchTransports(@RequestBody SearchRequest searchRequest) {
        notNull(searchRequest, "searchRequest cannot be null");

        co.elastic.clients.elasticsearch.core.SearchRequest esSearchRequest =
                searchRequestConverter.buildSearchRequest(searchRequest);

        return elasticSearchTransportClient.search(esSearchRequest);
    }
}
