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
		Recommendation recommendation,

		Damage damage
) {
	public record Damage(
			boolean hotSpots,
			boolean microCracks,
			boolean snailTrails,
			boolean brokenGlass,
			String additionalDetails
	) {}
}
