package org.transport.trade.elastic;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Profile("test")
public class EsTestConfig {

    @Bean
    public EsDataInitializer esDataInitializer(ElasticSearchTransportClient elasticSearchTransportClient) {
        return new EsDataInitializer(elasticSearchTransportClient);
    }
}
