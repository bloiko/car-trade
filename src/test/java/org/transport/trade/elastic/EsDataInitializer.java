package org.transport.trade.elastic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.transport.trade.transport.Transport;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@DependsOn("elasticTestContainerInitializer")
public class EsDataInitializer {

    private static boolean containerInitialized = false;

    private final ElasticSearchTransportClient elasticSearchTransportClient;

    private final ObjectMapper objectMapper;

    @Value("${es.mapping.file:/esMapping.json}")
    private String esMappingFile;

    @Value("${es.data.file:/initialEsData.json}")
    private String esDataFile;

    public EsDataInitializer(ElasticSearchTransportClient elasticSearchTransportClient, ObjectMapper objectMapper) {
        this.elasticSearchTransportClient = elasticSearchTransportClient;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        if (!containerInitialized) {
            initializeMapping();
            initializeTransports();
            containerInitialized = true;
        }
    }

    private void initializeMapping() {
        try (InputStream inputStreamEsMapping = getClass().getResourceAsStream(esMappingFile)) {
            if (inputStreamEsMapping == null) {
                System.out.println("Mapping file not found!");
                return;
            }
            elasticSearchTransportClient.initializeMapping(inputStreamEsMapping);
            System.out.println("Mapping initialized successfully.");
        } catch (IOException e) {
            System.out.println("Failed to initialize mapping: " + e.getMessage());
        }
    }

    private void initializeTransports() {
        try (InputStream inputStream = getClass().getResourceAsStream(esDataFile)) {
            if (inputStream == null) {
                System.out.println("Data file not found!");
                return;
            }

            List<Transport> transports = objectMapper.readValue(inputStream, new TypeReference<List<Transport>>() {
            });
            elasticSearchTransportClient.bulkIndex(transports);
            System.out.println("Transports saved successfully.");
        } catch (IOException e) {
            System.out.println("Unable to save transports: " + e.getMessage());
        }
    }
}
