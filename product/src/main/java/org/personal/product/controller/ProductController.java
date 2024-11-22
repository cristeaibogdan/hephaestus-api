package org.personal.product.controller;

import lombok.RequiredArgsConstructor;
import org.personal.product.dto.GetModelAndTypeResponse;
import org.personal.product.dto.GetProductIdentificationResponse;
import org.personal.product.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
class ProductController {

	private final ProductService productService;

	@GetMapping("/{category}/manufacturers")
	List<String> getManufacturers(@PathVariable String category) {
		return productService.getManufacturers(category);
	}

	@GetMapping("/{manufacturer}/models-and-types")
	List<GetModelAndTypeResponse> getModelsAndTypes(@PathVariable String manufacturer) {
		return productService.getModelsAndTypes(manufacturer);
	}

	public GetProductIdentificationResponse getProductIdentification(String qrCode) {
		return productService.getProductIdentification(qrCode);
	}
}
