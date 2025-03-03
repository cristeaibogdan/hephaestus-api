package org.personal.solarpanel.service;

import org.junit.jupiter.api.Test;
import org.personal.solarpanel.BaseIntegrationTest;
import org.personal.solarpanel.dto.SaveSolarPanelDamageRequest;
import org.personal.solarpanel.dto.SaveSolarPanelRequest;
import org.personal.solarpanel.entity.SolarPanel;
import org.personal.solarpanel.repository.SolarPanelRepository;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class SaveIntegrationTest extends BaseIntegrationTest {

	@Autowired SolarPanelApplicationService underTest;
	@Autowired SolarPanelRepository repository;

	@Test
	void should_saveRequest_With_AllPropertiesInDB() {
		// GIVEN
		SaveSolarPanelRequest request = new SaveSolarPanelRequest(
				"Solar Panel",
				"Tesla",
				"model1",
				"type2",
				"AX-098-200",
				new SaveSolarPanelDamageRequest(
						true,
						false,
						true,
						false
				)
		);

		// WHEN
		underTest.save(request);

		// THEN
		SolarPanel actual = repository.findBySerialNumber("AX-098-200")
				.orElseThrow();

		assertThat(actual).satisfies(act -> {
			// main fields
			assertThat(act.getSerialNumber()).isEqualTo(request.serialNumber());
		});
	}
}
