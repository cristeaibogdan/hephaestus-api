package org.personal.solarpanel.service;

import lombok.RequiredArgsConstructor;
import org.personal.solarpanel.dto.SaveSolarPanelRequest;
import org.personal.solarpanel.entity.SolarPanel;
import org.personal.solarpanel.repository.SolarPanelRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SolarPanelApplicationService {

	private final SolarPanelRepository repository;

	public void save(SaveSolarPanelRequest saveSolarPanelRequest) {
		SolarPanel solarPanel = new SolarPanel(
				saveSolarPanelRequest.serialNumber()
		);
		repository.save(solarPanel);
	}
}
