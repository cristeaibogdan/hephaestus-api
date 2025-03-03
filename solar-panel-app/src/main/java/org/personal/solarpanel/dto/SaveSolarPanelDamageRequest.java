package org.personal.solarpanel.dto;

public record SaveSolarPanelDamageRequest(
		boolean hotSpots,
		boolean microCracks,
		boolean snailTrails,
		boolean brokenGlass
) { }
