package org.personal.solarpanel.dto;

import org.personal.solarpanel.enums.Recommendation;

import java.time.LocalDateTime;

public record GetSolarPanelFullResponse(
		String category,
		String manufacturer,
		String model,
		String type,
		String serialNumber,
		LocalDateTime createdAt,
		Recommendation recommendation
) {}
