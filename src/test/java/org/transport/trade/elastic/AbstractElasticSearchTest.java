package org.transport.trade.elastic;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;

public abstract class AbstractElasticSearchTest {

    @Container
    protected static ElasticsearchContainer container = new ElasticTestContainer();

    @BeforeAll
    static void setUp() {
        container.start();
    }

    @AfterAll
    static void destroy() {
        container.stop();
    }
}
