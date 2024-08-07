package org.transport.trade.transport;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.transport.trade.elastic.AbstractElasticSearchTest;
import org.transport.trade.elastic.ElasticSearchTransportClientImpl;
import org.transport.trade.filter.Filters;
import org.transport.trade.filter.TextSearchFilter;
import org.transport.trade.transport.dto.TransportsResponse;
import org.transport.trade.transport.entity.Country;
import org.transport.trade.transport.entity.TransportType;

@AutoConfigureMockMvc
class TransportControllerTest extends AbstractElasticSearchTest {

    private static final Transport TRANSPORT = new Transport(
            "ID",
            TransportType.PASSENGER_CARS,
            "Sedan",
            Country.UKRAINE,
            2000,
            "BMW",
            "E3",
            new BigInteger("10000"),
            "Kyiv");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ElasticSearchTransportClientImpl elasticSearchTransportClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetTransportById() throws Exception {
        String savedTransportId = elasticSearchTransportClient.index(TRANSPORT);

        String responseBody = mockMvc.perform(get("/transports/transport/" + savedTransportId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(TRANSPORT), responseBody);
    }

    @Test
    void testAddTransport() {}

    @Test
    void testDeleteTransport() throws Exception {
        String savedTransportId = elasticSearchTransportClient.index(TRANSPORT);

        assertNotNull(elasticSearchTransportClient.getById(savedTransportId));

        mockMvc.perform(delete("/transports/transport/" + savedTransportId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNull(elasticSearchTransportClient.getById(savedTransportId));
    }

    @Test
    void testFilterTransports() throws Exception {
        Filters filters = new Filters();
        filters.getFilters().add(new TextSearchFilter("transportType", TransportType.BUSES.toString()));

        String filterRequest = objectMapper.writeValueAsString(filters);

        String responseBody = mockMvc.perform(post("/transports/filter")
                        .contentType("application/json")
                        .content(filterRequest))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        TransportsResponse response = objectMapper.readValue(responseBody, TransportsResponse.class);

        assertEquals(2, response.getTransports().size());
    }
}
