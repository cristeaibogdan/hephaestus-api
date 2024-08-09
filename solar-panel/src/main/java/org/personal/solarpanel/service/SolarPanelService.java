package org.personal.solarpanel.service;

import lombok.RequiredArgsConstructor;
import org.personal.solarpanel.entity.SolarPanel;
import org.personal.solarpanel.repository.SolarPanelRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SolarPanelService {

	private final SolarPanelRepository solarPanelRepository;

	public void saveSolarPanel(SolarPanel solarPanel) {

		boolean existingSerialNumber = solarPanelRepository.existsBySerialNumber(solarPanel.getSerialNumber());
		if (existingSerialNumber) {
			throw new RuntimeException("Serial number is taken");
		}

		solarPanelRepository.save(solarPanel);
	}
}
