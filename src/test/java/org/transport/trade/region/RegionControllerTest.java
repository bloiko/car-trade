package org.transport.trade.region;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.transport.trade.elastic.AbstractElasticSearchTest;
import org.transport.trade.elastic.ElasticSearchTransportClientImpl;

@AutoConfigureMockMvc
class RegionControllerTest extends AbstractElasticSearchTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ElasticSearchTransportClientImpl elasticSearchTransportClient;

    @Test
    void getRegionsSuggestion() throws Exception {
        elasticSearchTransportClient.getById("SOME_ID");

        String responseBody = mockMvc.perform(get("/regions").param("textSearch", "Some"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List response = objectMapper.readValue(responseBody, List.class);

        assertEquals(2, response.size());
        assertEquals(List.of("Some 123", "Some region"), response);
    }
}
