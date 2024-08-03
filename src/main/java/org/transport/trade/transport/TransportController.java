package org.transport.trade.transport;

import static org.apache.commons.lang3.Validate.notNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.transport.trade.filter.Filters;
import org.transport.trade.filter.convertor.FiltersConverter;
import org.transport.trade.service.elastic.ElasticSearchTransportClient;
import org.transport.trade.transport.dto.TransportsResponse;

@RestController
@RequestMapping("/transports")
public class TransportController {

    private final ElasticSearchTransportClient elasticSearchTransportClient;

    private final FiltersConverter filtersConverter;

    @Autowired
    public TransportController(
            ElasticSearchTransportClient elasticSearchTransportClient, FiltersConverter filtersConverter) {
        this.elasticSearchTransportClient = elasticSearchTransportClient;
        this.filtersConverter = filtersConverter;
    }

    @GetMapping("/transport/{transportId}")
    Transport getTransportById(@PathVariable String transportId) {
        notNull(transportId, "transportId cannot be null");

        return elasticSearchTransportClient.getById(transportId);
    }

    @PostMapping("/transport")
    void addTransport(@RequestBody Transport transport) {
        notNull(transport, "Request cannot be null");

        elasticSearchTransportClient.index(transport);
    }

    @DeleteMapping("/transport/{transportId}")
    void deleteTransport(@PathVariable String transportId) {
        notNull(transportId, "transportId cannot be null");

        elasticSearchTransportClient.deleteById(transportId);
    }

    @PostMapping("/filter")
    TransportsResponse filterTransports(@RequestBody Filters filters) {
        notNull(filters, "Filters cannot be null");

        co.elastic.clients.elasticsearch.core.SearchRequest esSearchRequest = filtersConverter.convert(filters);

        return elasticSearchTransportClient.search(esSearchRequest);
    }
}
