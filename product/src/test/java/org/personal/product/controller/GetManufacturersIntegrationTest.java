package org.personal.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.personal.product.BaseIntegrationTest;
import org.personal.product.entity.Product;
import org.personal.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GetManufacturersIntegrationTest extends BaseIntegrationTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@Autowired ProductController underTest;
	@Autowired ProductRepository repository;

	@BeforeAll
	void loadDataInDB() {
		cleanUpDB(); // Remove data already present due to flyway migration files
		List<Product> products = List.of(
				new Product("Washing Machine", "ManufacturerA", "ModelA", "TypeA", "QWE-001"),
				new Product("Washing Machine", "ManufacturerB", "ModelB", "TypeB", "QWE-002"),
				new Product("Washing Machine", "ManufacturerC", "ModelC", "TypeC", "QWE-003"),
				new Product("Washing Machine", "ManufacturerD", "ModelD", "TypeD", "QWE-004")
		);
		repository.saveAll(products);
	}

	@AfterAll
	void cleanUpDB() {
		repository.deleteAll();
	}

	@BeforeEach
	void checkInitialDataInDB() {
		assertThat(repository.count()).isEqualTo(4);
	}

	@Test
	void should_ReturnListOfManufacturers_By_Category() {
		// GIVEN
		List<String> expected = List.of("ManufacturerA", "ManufacturerB", "ManufacturerC", "ManufacturerD");

		// WHEN
		List<String> actual = underTest.getManufacturers("Washing Machine");

		// THEN
		assertThat(actual)
				.containsExactlyInAnyOrderElementsOf(expected);
	}

	@Nested
	class MvcTest {

		@Test
		void should_ThrowCustomException_When_CategoryNotFound() throws Exception {
			// GIVEN
			// WHEN
			ResultActions resultActions = performRequest("I don't exist");

			// THEN
			resultActions
					.andExpect(status().isNotFound())
					.andExpect(content().string(not(containsString("Internal Translation Error"))));
		}

		@Test
		void should_ReturnStatusOk_When_CategoryFound() throws Exception {
			// GIVEN
			// WHEN
			ResultActions resultActions = performRequest("Washing Machine");

			// THEN
			resultActions
					.andExpect(status().isOk())
					.andExpect(content().string(not(emptyString())));
		}

		private ResultActions performRequest(String category) throws Exception {
			return mockMvc.perform(
					get("/api/v1/products/{category}/manufacturers", category));
		}
	}
}
