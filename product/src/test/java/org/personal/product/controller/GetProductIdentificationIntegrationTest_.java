package org.personal.product.controller;

import org.junit.jupiter.api.*;
import org.personal.product.BaseIntegrationTest;
import org.personal.product.entity.Product;
import org.personal.product.repository.ProductRepository;
import org.personal.shared.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Disabled @Deprecated
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GetProductIdentificationIntegrationTest_ extends BaseIntegrationTest {

	@Autowired ProductController underTest;
	@Autowired ProductRepository productRepository;

	@BeforeAll
	void loadDataInDB() {
		List<Product> products = List.of(
				new Product("Washing Machine", "Bosch", "model1", "type1", "hephaestus-washing-machine-001"),
				new Product("Washing Machine", "WhirlPool", "model2", "type2", "hephaestus-washing-machine-002")
		);

		productRepository.saveAll(products);
	}

	@AfterAll
	void cleanUpDB() {
		productRepository.deleteAll();
	}

	@Test
	void should_ThrowCustomException_When_QrCodeNotFound() {
		// GIVEN
		String qrCode = "something NOT in DB";

		// WHEN & THEN
		assertThatThrownBy(() -> underTest.getProductIdentification(qrCode))
				.isInstanceOf(CustomException.class);
	}

}