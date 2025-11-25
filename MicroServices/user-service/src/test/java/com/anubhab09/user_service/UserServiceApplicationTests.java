package com.anubhab09.user_service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Disabled for CI — requires real DB/Redis")
class UserServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
