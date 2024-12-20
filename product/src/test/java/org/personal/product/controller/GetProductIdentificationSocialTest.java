package org.personal.product.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.personal.product.dto.GetProductIdentificationResponse;
import org.personal.product.repository.ProductRepository;
import org.personal.product.service.ProductService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Disabled @Deprecated
class GetProductIdentificationSocialTest {

	ProductRepository productRepositoryMock = mock(ProductRepository.class);
	ProductService productService = new ProductService(productRepositoryMock);
	ProductController underTest = new ProductController(productService);

	@Test
	void should_ReturnGetProductIdentification_When_ProvidedQrCode() {
		// GIVEN
		GetProductIdentificationResponse expected = new GetProductIdentificationResponse(
				"WhirlPool",
				"Model100",
				"Type100"
		);

		String qrCode = "hephaestus-washing-machine-001";

		given(productRepositoryMock.findByQrCode(qrCode)).willReturn(Optional.of(expected));

		// WHEN
		GetProductIdentificationResponse actual = underTest.getProductIdentification(qrCode);

		// THEN
		verify(productRepositoryMock).findByQrCode(qrCode);

		assertThat(actual)
				.usingRecursiveComparison()
				.isEqualTo(expected);
	}

}