package org.transport.trade.elastic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public abstract class AbstractElasticSearchTest {

    @Autowired
    protected EsDataInitializer esDataInitializer;
}
