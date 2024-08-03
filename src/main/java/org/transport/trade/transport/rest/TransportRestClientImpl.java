package org.transport.trade.transport.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.transport.trade.transport.dto.TransportDto;
import org.transport.trade.transport.dto.TransportResultsDto;

@Slf4j
@Component
public class TransportRestClientImpl implements TransportRestClient {

    private final ObjectMapper objectMapper;

    private final String carApiApplicationId;

    private final String carApiSecretKey;

    public TransportRestClientImpl(
            @Autowired ObjectMapper objectMapper,
            @Value("${car.api.app.id}") String carApiApplicationId,
            @Value("${car.api.secret.key}") String carApiSecretKey) {
        this.objectMapper = objectMapper;
        this.carApiApplicationId = carApiApplicationId;
        this.carApiSecretKey = carApiSecretKey;
    }

    @Override
    public List<TransportDto> getTransports(int pageSize, int pageNum) {
        try {
            URL url = new URL(String.format(
                    "https://parseapi.back4app.com/classes/Carmodels_Car_Model_List?skip=%d&limit=%d&excludeKeys=Year",
                    pageSize * (pageNum - 1), pageSize));

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                urlConnection.setRequestProperty("X-Parse-Application-Id", carApiApplicationId);
                urlConnection.setRequestProperty("X-Parse-REST-API-Key", carApiSecretKey);
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                TransportResultsDto transportResultsDto =
                        objectMapper.readValue(stringBuilder.toString(), TransportResultsDto.class);
                return transportResultsDto.getResults();
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            log.error("Error: {}", String.valueOf(e));
        }
        return Collections.emptyList();
    }
}
