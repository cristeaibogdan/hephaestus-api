package org.personal.solarpanel.service;

import lombok.RequiredArgsConstructor;
import org.personal.solarpanel.dto.SolarPanelDTO;
import org.personal.solarpanel.repository.SolarPanelRepository;

@RequiredArgsConstructor
public class SolarPanelApplication {

	private final SolarPanelRepository solarPanelRepository;

	public void save(String solarPanel, String red, int i) {
		solarPanelRepository.save(solarPanel, red, i);
	}

	public SolarPanelDTO get(Long id) {
		return solarPanelRepository.findById(id);
	}
}
