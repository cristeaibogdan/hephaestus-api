package org.personal.solarpanel.dto;

public record SaveSolarPanelRequest(
		String category,
		String manufacturer,
		String model,
		String type,
		String serialNumber,

		SaveSolarPanelDamageRequest saveSolarPanelDamageRequest
) { }
