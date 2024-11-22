package org.personal.product.dto;

public record GetProductIdentificationResponse(
		String manufacturer,
		String model,
		String type) { }