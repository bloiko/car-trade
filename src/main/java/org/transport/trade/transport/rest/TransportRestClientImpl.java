package org.transport.trade.transport.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.transport.trade.transport.dto.TransportDto;
import org.transport.trade.transport.dto.TransportResultsDto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class TransportRestClientImpl implements TransportRestClient {

    private final ObjectMapper objectMapper;

    private final String carApiApplicationId;
    private final String carApiSecretKey;

    public TransportRestClientImpl(ObjectMapper objectMapper,
            @Value("${car.api.app.id}") String carApiApplicationId,
            @Value("${car.api.secret.key}") String carApiSecretKey) {
        this.objectMapper = objectMapper;
        this.carApiApplicationId = carApiApplicationId;
        this.carApiSecretKey = carApiSecretKey;
    }

    @Override
    public List<TransportDto> getTransports(int pageSize, int pageNum) {
        String apiUrl =
                String.format("https://parseapi.back4app.com/classes/Carmodels_Car_Model_List?skip=%d&limit=%d&excludeKeys=Year", pageSize * (pageNum - 1), pageSize);

        try {
            HttpURLConnection urlConnection = createConnection(apiUrl);
            return fetchTransports(urlConnection);
        } catch (Exception e) {
            log.error("Error fetching transports: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    private HttpURLConnection createConnection(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("X-Parse-Application-Id", carApiApplicationId);
        urlConnection.setRequestProperty("X-Parse-REST-API-Key", carApiSecretKey);
        return urlConnection;
    }

    private List<TransportDto> fetchTransports(HttpURLConnection urlConnection) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
            String response = reader.lines().reduce("", String::concat);
            TransportResultsDto transportResultsDto = objectMapper.readValue(response, TransportResultsDto.class);
            return transportResultsDto.getResults();
        } finally {
            urlConnection.disconnect();
        }
    }
}
