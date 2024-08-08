package org.transport.trade.elastic;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Profile("test")
public class EsTestConfig {

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public EsDataInitializer esDataInitializer(ElasticSearchTransportClient elasticSearchTransportClient) {
        return new EsDataInitializer(elasticSearchTransportClient, objectMapper);
    }
}
