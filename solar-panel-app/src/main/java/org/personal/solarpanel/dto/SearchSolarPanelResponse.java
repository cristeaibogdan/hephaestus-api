package org.personal.solarpanel.dto;

import org.personal.solarpanel.enums.Recommendation;

import java.time.LocalDateTime;

public record SearchSolarPanelResponse(
		String category,
		String manufacturer,

		String model,
		String type,
		String serialNumber,

		Recommendation recommendation,
		LocalDateTime createdAt
) { }
