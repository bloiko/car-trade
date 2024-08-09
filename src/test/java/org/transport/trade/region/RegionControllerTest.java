package org.transport.trade.region;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.transport.trade.elastic.AbstractElasticSearchTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class RegionControllerTest extends AbstractElasticSearchTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getRegionsSuggestion() throws Exception {
        String responseBody = performGet("/regions", "Some");

        List<String> response = parseJsonResponse(responseBody, new TypeReference<>() {
        });
        assertEquals(List.of("Some 123", "Some region"), response);
    }

    private String performGet(String url, String regionParam) throws Exception {
        return mockMvc.perform(get(url).param("textSearch", regionParam))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    private <T> T parseJsonResponse(String json, TypeReference<T> typeReference) throws Exception {
        return objectMapper.readValue(json, typeReference);
    }
}
