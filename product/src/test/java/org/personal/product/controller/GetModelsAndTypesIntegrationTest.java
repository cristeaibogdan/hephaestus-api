package org.personal.product.controller;

import org.junit.jupiter.api.*;
import org.personal.product.BaseIntegrationTest;
import org.personal.product.dto.GetModelAndTypeResponse;
import org.personal.product.entity.Product;
import org.personal.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

	@Test
	void should_ReturnListOfModelsAndTypes_When_ManufacturerFoundInDB() {
		// GIVEN
		List<GetModelAndTypeResponse> expected = List.of(
				new GetModelAndTypeResponse("ModelA", "TypeA"),
				new GetModelAndTypeResponse("ModelB", "TypeB")
		);

		// WHEN
		List<GetModelAndTypeResponse> actual = underTest.getModelsAndTypes("Orokin");

		// THEN
		assertThat(actual)
				.containsExactlyInAnyOrderElementsOf(expected);
	}
}
