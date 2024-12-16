package org.personal.washingmachine;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;

/*
	@Transactional
	on class = is applied on all methods; every @Test + @BeforeEach and anything before
 	Executes in a transaction. When you finish a @Test, it ROLLSBACK, it DOES NOT COMMIT to DB.

 	Clean and automated, but you may miss:
 		- Unique at insert/update
 		- FK violations
 		- not null
 		- other DB triggers

 	Limitations: Can't test code that uses:
 		- @Async
 		- @Transactional(REQUIRES_NEW)
*/

@Tag("slow")
@AutoConfigureMockMvc // avoids swapping threads in Tomcat's tread pool so @Transactional still works
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIntegrationTest {

	@ServiceConnection
	private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

	static {
		postgres.start();
	}
}
