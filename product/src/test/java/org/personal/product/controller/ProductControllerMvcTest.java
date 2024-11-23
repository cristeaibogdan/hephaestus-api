package org.personal.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.personal.product.dto.GetProductIdentificationResponse;
import org.personal.product.service.ProductService;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerMvcTest {
	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;
	@MockBean ProductService productService;

	@Test
	void should_ReturnStatusOk_When_ProvidedQrCode() throws Exception {
		// GIVEN
		String qrCode = "hephaestus-washing-machine-001";

		GetProductIdentificationResponse expected = new GetProductIdentificationResponse(
				"Bosch",
				"model100",
				"Type192"
		);

		given(productService.getProductIdentification(qrCode)).willReturn(expected);

		// WHEN
		ResultActions resultActions = mockMvc.perform(
				get("/api/v1/products/{qrCode}", qrCode));

		// THEN
		resultActions
				.andExpect(status().isOk())
				.andExpect(content().string(jackson.writeValueAsString(expected)));
	}

	@Test
	void should_ReturnStatusNotFound_When_QrCodeNotFound() throws Exception {
		// GIVEN
		String qrCode = "hephaestus-washing-machine-001";

		given(productService.getProductIdentification(qrCode))
				.willThrow(new CustomException(ErrorCode.QR_CODE_NOT_FOUND));

		// WHEN
		ResultActions resultActions = mockMvc.perform(
				get("/api/v1/products/{qrCode}", qrCode));

		// THEN
		resultActions
				.andExpect(status().isNotFound())
				.andExpect(content().string(not(containsString("Internal Translation Error"))));
	}
}