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

class GetModelsAndTypesIntegrationTest extends BaseIntegrationTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@Autowired ProductController underTest;
	@Autowired ProductRepository repository;

	@BeforeEach
	void checkNoDataInDB() {
		repository.deleteAll(); // Remove data already present due to flyway migration files
		assertThat(repository.count()).isZero();
	}

	@MethodSource("provideModelsAndTypesTestCases")
	@ParameterizedTest(name = "Found models and types for manufacturer {0}")
	void should_ReturnListOfModelsAndTypes_When_ManufacturerFound(String manufacturer, List<GetModelAndTypeResponse> expected) {
		// GIVEN
		insertIntoDB(
				new Product("Washing Machine", "Orokin", "ModelA", "TypeA", "QWE-001"),
				new Product("Washing Machine", "Orokin", "ModelB", "TypeB", "QWE-002"),
				new Product("Washing Machine", "LG", "ModelC", "TypeC", "QWE-003"),
				new Product("Washing Machine", "LG", "ModelD", "TypeD", "QWE-004")
		);

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

	private void insertIntoDB(Product... products) {
		repository.saveAll(List.of(products));
	}

	@Nested
	class MvcTest {

		@Test
		void should_ThrowCustomException_When_ManufacturerNotFound() throws Exception {
			// GIVEN
			// WHEN
			ResultActions resultActions = performRequest("I don't exist");

			// THEN
			resultActions
					.andExpect(status().isNotFound())
					.andExpect(content().string(not(containsString("Internal Translation Error"))));
		}

		@Test
		void should_ReturnStatusOk_When_ManufacturerFound() throws Exception {
			// GIVEN
			insertIntoDB(
					new Product("Washing Machine", "Orokin", "ModelA", "TypeA", "QWE-001")
			);

			// WHEN
			ResultActions resultActions = performRequest("Orokin");

			// THEN
			resultActions
					.andExpect(status().isOk())
					.andExpect(content().string(not(emptyString())));
		}

		private ResultActions performRequest(String manufacturer) throws Exception {
			return mockMvc.perform(
					get("/v1/products/{manufacturer}/models-and-types", manufacturer));
		}
	}
}
