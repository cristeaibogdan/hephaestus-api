package org.personal.product.controller;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.personal.product.BaseIntegrationTest;
import org.personal.product.dto.GetProductIdentificationResponse;
import org.personal.product.entity.Product;
import org.personal.product.repository.ProductRepository;
import org.personal.product.service.ProductService;
import org.personal.shared.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductControllerIntegrationTest extends BaseIntegrationTest {

	@Autowired ProductController underTest;
	@Autowired ProductRepository productRepository;

	@BeforeAll
	void loadDataInDB() {
		List<Product> products = new ArrayList<>();
		products.add(new Product("Washing Machine", "Bosch", "model1", "type1", "hephaestus-washing-machine-001"));
		products.add(new Product("Washing Machine", "WhirlPool", "model2", "type2", "hephaestus-washing-machine-002"));
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