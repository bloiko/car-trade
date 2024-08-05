package org.transport.trade.transport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.transport.trade.brand.BrandRepository;
import org.transport.trade.elastic.ElasticSearchTransportClient;
import org.transport.trade.transport.entity.Country;
import org.transport.trade.transport.entity.TransportType;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

@RestController
@RequestMapping("/generator")
public class TransportsGeneratorController {

    private final BrandRepository brandRepository;

    private final ElasticSearchTransportClient elasticSearchTransportClient;

    @Autowired
    public TransportsGeneratorController(
            BrandRepository brandRepository, ElasticSearchTransportClient elasticSearchTransportClient) {
        this.brandRepository = brandRepository;
        this.elasticSearchTransportClient = elasticSearchTransportClient;
    }

    @GetMapping("/transports/db-elastic-sync")
    void generateTransports() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Arrays.stream(Country.values()).forEach(country -> {
            brandRepository.findAll().forEach(brand -> {
                Transport transport = Transport.builder()
                        .transportType(TransportType.PASSENGER_CARS)
                        .bodyType(brand.getBodyType())
                        .brand(brand.getName())
                        .manufacturerCountry(country)
                        .region("Some region")
                        .price(BigInteger.valueOf(random.nextInt() % 1000))
                        .model(brand.getModelName())
                        .manufacturerYear(2020)
                        .build();
                elasticSearchTransportClient.index(transport);
            });
        });
    }
}
