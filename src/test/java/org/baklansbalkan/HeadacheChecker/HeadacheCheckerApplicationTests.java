package org.baklansbalkan.HeadacheChecker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class HeadacheCheckerApplicationTests {

    @Test
    @DisplayName(value = "The context is initialized successfully")
    void contextLoads() {
    }
}
