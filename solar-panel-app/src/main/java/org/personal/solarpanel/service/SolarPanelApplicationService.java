package org.personal.solarpanel.service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SolarPanelApplicationService {

	private final SolarPanelRepository repository;

	public void save(SolarPanel solarPanel) {
		repository.save(solarPanel);
	}

	public GetSolarPanelExpanded getExpanded(String serialNumber) {
		repository.findBySerialNumber(serialNumber);
		return new GetSolarPanelExpanded();
	}
}
