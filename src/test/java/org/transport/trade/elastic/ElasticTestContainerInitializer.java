package org.transport.trade.elastic;

import org.springframework.stereotype.Component;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;

@Component
public class ElasticTestContainerInitializer {

    @Container
    private static final ElasticsearchContainer container = new ElasticTestContainer();

    private static boolean containerStarted = false;

    public ElasticTestContainerInitializer() {
        if (!containerStarted) {
            container.start();
            containerStarted = true;
            System.out.println("Elasticsearch container started.");
        }
    }
}
