package org.personal.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.personal.product.BaseIntegrationTest;
import org.personal.product.dto.GetModelAndTypeResponse;
import org.personal.product.entity.Product;
import org.personal.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GetModelsAndTypesIntegrationTest extends BaseIntegrationTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@Autowired ProductController underTest;
	@Autowired ProductRepository productRepository;

	@BeforeAll
	void loadDataInDB() {
		List<Product> products = List.of(
				new Product("Washing Machine", "Orokin", "ModelA", "TypeA", "QWE-001"),
				new Product("Washing Machine", "Orokin", "ModelB", "TypeB", "QWE-002"),
				new Product("Washing Machine", "LG", "ModelC", "TypeC", "QWE-003"),
				new Product("Washing Machine", "LG", "ModelD", "TypeD", "QWE-004")
		);
		productRepository.saveAll(products);
	}

	@AfterAll
	void cleanUpDB() {
		productRepository.deleteAll();
	}

	@BeforeEach
	void dataPresentInDB() {
		// some data is already present due to flyway migration files
		assertThat(productRepository.count()).isEqualTo(18);
	}

	@MethodSource("provideModelsAndTypesTestCases")
	@ParameterizedTest(name = "Found models and types for manufacturer {0}")
	void should_ReturnListOfModelsAndTypes_When_ManufacturerFoundInDB(String manufacturer, List<GetModelAndTypeResponse> expected) {
		// GIVEN

		// WHEN
		List<GetModelAndTypeResponse> actual = underTest.getModelsAndTypes(manufacturer);

		// THEN
		assertThat(actual)
				.containsExactlyInAnyOrderElementsOf(expected);
	}

	static Stream<Arguments> provideModelsAndTypesTestCases() {
		return Stream.of(
				arguments("Orokin", List.of(
						new GetModelAndTypeResponse("ModelA", "TypeA"),
						new GetModelAndTypeResponse("ModelB", "TypeB")
				)),
				arguments("LG", List.of(
						new GetModelAndTypeResponse("ModelC", "TypeC"),
						new GetModelAndTypeResponse("ModelD", "TypeD")
				))
		);
	}

	@Nested
	class MvcTest {

		@Test
		void should_ThrowCustomException_When_ManufacturerNotFound() throws Exception {
			// GIVEN
			String manufacturer = "I don't exist";

			// WHEN
			ResultActions resultActions = performRequest(manufacturer);

			// THEN
			resultActions
					.andExpect(status().isNotFound())
					.andExpect(content().string(not(containsString("Internal Translation Error"))));
		}

		@Test
		void should_ReturnStatusOk_When_ManufacturerFound() throws Exception {
			// GIVEN
			String manufacturer = "Orokin";

			// WHEN
			ResultActions resultActions = performRequest(manufacturer);

			// THEN
			resultActions
					.andExpect(status().isOk())
					.andExpect(content().string(not(emptyString())));
		}

		private ResultActions performRequest(String request) throws Exception {
			return mockMvc.perform(
					get("/api/v1/products/{manufacturer}/models-and-types", request));
		}
	}
}
