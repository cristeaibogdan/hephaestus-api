package org.personal.solarpanel.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record SaveSolarPanelRequest(
		String category,
		String manufacturer,
		String model,
		String type,
		String serialNumber,

		SaveSolarPanelDamageRequest saveSolarPanelDamageRequest
) { }
