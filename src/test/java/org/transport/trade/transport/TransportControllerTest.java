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
import org.transport.trade.filter.RangeFilter;
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

    private static final Transport NEW_TRANSPORT = new Transport(
            "TRANSPORT_ID_TESLA_AIR",
            TransportType.AIR_TRANSPORT,
            "Sedan",
            Country.CHINA,
            2020,
            "Tesla",
            "Model S",
            new BigInteger("10000"),
            "Region test");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ElasticSearchTransportClientImpl elasticSearchClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetTransportById() throws Exception {
        String id = elasticSearchClient.index(TRANSPORT);

        String response = performGet("/transports/" + id);

        assertEquals(asJsonString(TRANSPORT), response);
    }

    @Test
    void testAddTransport() throws Exception {
        assertNull(elasticSearchClient.getById("TRANSPORT_ID_TESLA_AIR"));

        performPost("/transports", NEW_TRANSPORT);

        Transport addedTransport = elasticSearchClient.getById("TRANSPORT_ID_TESLA_AIR");
        assertNotNull(addedTransport);
        assertEquals(NEW_TRANSPORT, addedTransport);
    }

    @Test
    void testDeleteTransport() throws Exception {
        String id = elasticSearchClient.index(TRANSPORT);
        assertNotNull(elasticSearchClient.getById(id));

        performDelete("/transports/" + id);

        assertNull(elasticSearchClient.getById(id));
    }

    @Test
    void testFilterTransports() throws Exception {
        Filters filters = new Filters();
        filters.getFilters().add(new TextSearchFilter("transportType", TransportType.BUSES.toString()));

        String response = performPost("/transports/filter", filters);

        TransportsResponse responseObj = objectMapper.readValue(response, TransportsResponse.class);
        assertEquals(2, responseObj.getTransports().size());
    }

    @Test
    void testFilterTransportsWithDateRange() throws Exception {
        Filters filters = new Filters();
        filters.getFilters().add(new RangeFilter("manufacturerYear", "1899", "1901"));

        String response = performPost("/transports/filter", filters);

        TransportsResponse responseObj = objectMapper.readValue(response, TransportsResponse.class);
        assertEquals(1, responseObj.getTransports().size());
    }

    private String asJsonString(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    private String performGet(String url) throws Exception {
        return mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    private String performPost(String url, Object content) throws Exception {
        return mockMvc.perform(post(url).contentType("application/json").content(asJsonString(content)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    private void performDelete(String url) throws Exception {
        mockMvc.perform(delete(url))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}
