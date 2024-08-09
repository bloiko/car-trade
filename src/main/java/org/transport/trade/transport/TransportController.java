package org.transport.trade.transport;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.transport.trade.elastic.ElasticSearchTransportClient;
import org.transport.trade.filter.Filters;
import org.transport.trade.filter.convertor.FiltersConverter;
import org.transport.trade.transport.dto.TransportsResponse;

@RestController
@RequestMapping("/transports")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransportController {

    private final ElasticSearchTransportClient elasticSearchTransportClient;

    private final FiltersConverter filtersConverter;

    @GetMapping("/{transportId}")
    Transport getTransportById(@PathVariable String transportId) {
        return elasticSearchTransportClient.getById(transportId);
    }

    @PostMapping
    void addTransport(@RequestBody Transport transport) {
        elasticSearchTransportClient.index(transport);
    }

    @DeleteMapping("/{transportId}")
    void deleteTransport(@PathVariable String transportId) {
        elasticSearchTransportClient.deleteById(transportId);
    }

    @PostMapping("/filter")
    public TransportsResponse filterTransports(@RequestBody Filters filters) {
        co.elastic.clients.elasticsearch.core.SearchRequest esSearchRequest = filtersConverter.convert(filters);

        return elasticSearchTransportClient.search(esSearchRequest);
    }
}
