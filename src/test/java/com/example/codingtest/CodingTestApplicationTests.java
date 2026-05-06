package com.example.codingtest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.cache.type=simple",
		"app.kafka.enabled=false"
})
class CodingTestApplicationTests {

	@Test
	void contextLoads() {
	}

}
