package org.transport.trade;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class TransportTradeApplicationTests {

    @Test
    void contextLoads() {
        // empty test
        assertTrue(true);
    }
}
