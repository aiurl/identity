package com.nerosoft.linkyou.utility;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class SnowflakeIdTests {
    @Test
    void generateId_returnsUniqueIds() {
        SnowflakeId generator = new SnowflakeId(1, 1);

        long id1 = generator.nextId();
        long id2 = generator.nextId();

        assert id1 != id2 : "Generated IDs should be unique";
    }
}
