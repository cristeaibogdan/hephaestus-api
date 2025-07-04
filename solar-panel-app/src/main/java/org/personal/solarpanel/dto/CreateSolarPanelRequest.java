package org.personal.solarpanel.dto;

import lombok.With;

@With
public record CreateSolarPanelRequest(
		String category,
		String manufacturer,
		String model,
		String type,
		String serialNumber,

		Damage damage
) {
	public record Damage(
			boolean hotSpots,
			boolean microCracks,
			boolean snailTrails,
			boolean brokenGlass,
			String additionalDetails
	) { }
}
