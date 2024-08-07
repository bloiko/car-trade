package org.transport.trade.elastic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.transport.trade.transport.Transport;

@Configuration
public class EsDataInitializer {

    private final ElasticSearchTransportClient elasticSearchTransportClient;

    public EsDataInitializer(ElasticSearchTransportClient elasticSearchTransportClient) {
        this.elasticSearchTransportClient = elasticSearchTransportClient;
    }

    @PostConstruct
    public void init() {
        System.out.println("Check ES health");
        int counter = 0;
        while (elasticSearchTransportClient == null || !elasticSearchTransportClient.healthCheck()) {
            try {
                Thread.sleep(100);
                if (counter++ > 5) {
                    System.out.println("Cannot initialize ES!!!!");
                    return;
                }
                counter++;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Initialize transports");
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Transport>> typeReference = new TypeReference<List<Transport>>() {};
        InputStream inputStream = getClass().getResourceAsStream("/initialEsData.json");

        if (inputStream == null) {
            System.out.println("File not found!");
            return;
        }

        try {
            List<Transport> transports = mapper.readValue(inputStream, typeReference);
            elasticSearchTransportClient.bulkIndex(transports);
            System.out.println("Transports saved successfully");
        } catch (IOException e) {
            System.out.println("Unable to save transports: " + e.getMessage());
        }
    }
}
