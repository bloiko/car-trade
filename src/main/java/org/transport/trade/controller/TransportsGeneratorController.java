package org.transport.trade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.transport.trade.entity.*;
import org.transport.trade.service.TransportDbSynchronizer;
import org.transport.trade.service.elastic.ElasticSearchTransportClient;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

@RestController
@RequestMapping("/generator")
public class TransportsGeneratorController {

    private final ElasticSearchTransportClient elasticSearchTransportClient;

    private final TransportDbSynchronizer transportDbSynchronizer;

    @Autowired
    public TransportsGeneratorController(ElasticSearchTransportClient elasticSearchTransportClient, TransportDbSynchronizer transportDbSynchronizer) {
        this.elasticSearchTransportClient = elasticSearchTransportClient;
        this.transportDbSynchronizer = transportDbSynchronizer;
    }

    @GetMapping("/transports")
    public void generateTransports() {
        Random random = new Random(100);
        Arrays.stream(BodyType.values()).forEach(bodyType -> {
            Arrays.stream(Brand.values()).forEach(brand -> {
                Arrays.stream(Country.values()).forEach(country -> {
                    Arrays.stream(TransportType.values()).forEach(transportType -> {
                        Transport transport = Transport.builder()
                                                       .transportType(TransportType.PASSENGER_CARS)
                                                       .bodyType(bodyType)
                                                       .brand(brand)
                                                       .manufacturerCountry(country)
                                                       .region("Kyiv")
                                                       .price(BigInteger.valueOf(random.nextInt() % 1000))
                                                       .model("Some model")
                                                       .manufacturerYear(2020)
                                                       .build();
                        elasticSearchTransportClient.index(transport);
                    });
                });
            });
        });
    }

    @GetMapping("/transports/sync-db")
    public void syncTransportsToDb() {
        transportDbSynchronizer.sync();
    }
}
