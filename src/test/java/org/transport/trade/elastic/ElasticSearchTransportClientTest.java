package org.transport.trade.elastic;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.transport.trade.transport.Transport;
import org.transport.trade.transport.entity.Country;
import org.transport.trade.transport.entity.TransportType;

@SpringBootTest
@ActiveProfiles("test")
class ElasticSearchTransportClientTest extends AbstractElasticSearchTest {

    private static final Transport TRANSPORT = new Transport(
            "ID",
            TransportType.PASSENGER_CARS,
            "Sedan",
            Country.CHINA,
            2000,
            "BMW",
            "E3",
            new BigInteger("10000"),
            "Kyiv");

    @Autowired
    private ElasticSearchTransportClientImpl elasticSearchTransportClient;

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
