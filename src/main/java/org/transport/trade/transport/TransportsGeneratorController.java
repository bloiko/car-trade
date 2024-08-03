package org.transport.trade.transport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.transport.trade.bodytype.BodyTypeRepository;
import org.transport.trade.brand.BrandRepository;
import org.transport.trade.model.ModelRepository;
import org.transport.trade.service.elastic.ElasticSearchTransportClient;
import org.transport.trade.transport.entity.Country;
import org.transport.trade.transport.entity.TransportType;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

@RestController
@RequestMapping("/generator")
public class TransportsGeneratorController {

    private final ModelRepository modelRepository;

    private final BrandRepository brandRepository;

    private final BodyTypeRepository bodyTypeRepository;

    private final ElasticSearchTransportClient elasticSearchTransportClient;

    private final TransportDbSynchronizer transportDbSynchronizer;

    @Autowired
    public TransportsGeneratorController(
            ModelRepository modelRepository,
            BrandRepository brandRepository,
            BodyTypeRepository bodyTypeRepository,
            ElasticSearchTransportClient elasticSearchTransportClient,
            TransportDbSynchronizer transportDbSynchronizer) {
        this.modelRepository = modelRepository;
        this.brandRepository = brandRepository;
        this.bodyTypeRepository = bodyTypeRepository;
        this.elasticSearchTransportClient = elasticSearchTransportClient;
        this.transportDbSynchronizer = transportDbSynchronizer;
    }

    @GetMapping("/transports/db-elastic-sync")
    public void generateTransports() {
        Random random = new Random(100);
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

    @GetMapping("/transports/sync-db")
    public void syncTransportsToDb() {
        transportDbSynchronizer.sync();
    }
}
