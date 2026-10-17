package application;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Disabled in local test run until live database is started via docker-compose")
class NexMarketApplicationTests {

	@Test
	void contextLoads() {
	}

}
