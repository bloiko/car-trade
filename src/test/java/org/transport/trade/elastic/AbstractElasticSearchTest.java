package org.transport.trade.elastic;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.transport.trade.TransportTradeApplication;

@SpringBootTest(classes = {TransportTradeApplication.class, EsTestConfig.class})
@ActiveProfiles("test")
public abstract class AbstractElasticSearchTest {

    @Container
    private static final ElasticsearchContainer container = new ElasticTestContainer();

    @BeforeAll
    static void setUp() {
        container.start();
    }

    @AfterAll
    static void tearDown() {
        container.stop();
    }
}
