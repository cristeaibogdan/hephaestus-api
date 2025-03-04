package org.personal.solarpanel.service;

import lombok.RequiredArgsConstructor;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.solarpanel.dto.SaveSolarPanelRequest;
import org.personal.solarpanel.entity.Damage;
import org.personal.solarpanel.entity.SolarPanel;
import org.personal.solarpanel.repository.SolarPanelRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@Service
@RestController
@RequiredArgsConstructor
public class SolarPanelApplicationService implements ISolarPanelApplicationService {

	private final SolarPanelRepository repository;

	@Override
	public void save(SaveSolarPanelRequest request) {
		SolarPanel solarPanel = new SolarPanel(
				request.category(),
				request.manufacturer(),
				request.model(),
				request.type(),
				request.serialNumber(),
				new Damage(
						request.saveSolarPanelDamageRequest().hotSpots(),
						request.saveSolarPanelDamageRequest().microCracks(),
						request.saveSolarPanelDamageRequest().snailTrails(),
						request.saveSolarPanelDamageRequest().brokenGlass(),
						request.saveSolarPanelDamageRequest().additionalDetails()
				)
		);

		boolean existingSerialNumber = repository.existsBySerialNumber(solarPanel.getSerialNumber());
		if (existingSerialNumber) {
			throw new CustomException(ErrorCode.SERIAL_NUMBER_ALREADY_TAKEN);
		}

		repository.save(solarPanel);
	}
}
