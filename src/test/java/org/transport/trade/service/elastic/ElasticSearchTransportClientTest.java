package org.transport.trade.service.elastic;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.transport.trade.transport.Transport;
import org.transport.trade.transport.entity.Country;
import org.transport.trade.transport.entity.TransportType;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ElasticSearchTransportClientTest {

    private static final Transport TRANSPORT =
            new Transport("ID", TransportType.PASSENGER_CARS, "Sedan", Country.CHINA, 2000, "BMW", "E3", new BigInteger("10000"), "Kyiv");

    @Autowired
    private ElasticSearchTransportClientImpl elasticSearchTransportClient;

    @Container
    protected static ElasticsearchContainer container = new ElasticTestContainer();

    @BeforeAll
    static void setUp() {
        container.start();
    }

    @AfterAll
    static void destroy() {
        container.stop();
    }

    @Test
    void testGetById() {
        String savedId = elasticSearchTransportClient.index(TRANSPORT);

        Transport actualTransport = elasticSearchTransportClient.getById(savedId);

        assertNotNull(actualTransport);
        assertEquals(savedId, actualTransport.getId());
        assertEquals(TRANSPORT, actualTransport);
    }

    @Test
    void testDeleteById() {
        String savedId = elasticSearchTransportClient.index(TRANSPORT);

        Transport actualTransport = elasticSearchTransportClient.getById(savedId);

        assertNotNull(actualTransport);
        assertEquals(savedId, actualTransport.getId());
        assertEquals(TRANSPORT, actualTransport);

        elasticSearchTransportClient.deleteById(savedId);

        actualTransport = elasticSearchTransportClient.getById(savedId);
        assertNull(actualTransport);
    }
}
