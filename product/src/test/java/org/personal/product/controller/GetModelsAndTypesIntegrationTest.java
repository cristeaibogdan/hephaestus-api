package org.personal.product.controller;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.personal.product.BaseIntegrationTest;
import org.personal.product.dto.GetModelAndTypeResponse;
import org.personal.product.entity.Product;
import org.personal.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GetModelsAndTypesIntegrationTest extends BaseIntegrationTest {

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
	void oneEntityInDB() {
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
}
