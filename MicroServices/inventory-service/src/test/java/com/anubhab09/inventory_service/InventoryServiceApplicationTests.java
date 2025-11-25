package com.anubhab09.inventory_service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Disabled for CI — requires real DB/Redis")
class InventoryServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
