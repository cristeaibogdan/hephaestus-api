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

	public void save(SaveSolarPanelRequest request) {
		SolarPanel solarPanel = new SolarPanel(
				request.category(),
				request.manufacturer(),
				request.model(),
				request.type(),
				request.serialNumber()
		);
		repository.save(solarPanel);
	}
}
