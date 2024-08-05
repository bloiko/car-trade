package org.transport.trade.region;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.transport.trade.elastic.ElasticSearchTransportClient;

import java.util.List;

@RestController
@RequestMapping("/regions")
public class RegionController {

    private final ElasticSearchTransportClient elasticSearchTransportClient;

    @Autowired
    public RegionController(ElasticSearchTransportClient elasticSearchTransportClient) {
        this.elasticSearchTransportClient = elasticSearchTransportClient;
    }

    @GetMapping
    List<String> getRegionsSuggestion(@RequestParam String textSearch) {
        return elasticSearchTransportClient.getSuggestions(textSearch, "region.suggest");
    }
}
