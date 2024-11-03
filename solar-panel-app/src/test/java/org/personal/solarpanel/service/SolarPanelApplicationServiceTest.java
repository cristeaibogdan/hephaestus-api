package org.personal.solarpanel.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SolarPanelApplicationServiceTest {

	@Test
	void should_SaveSolarPanel() {
		// GIVEN
		SolarPanelRepository repo = mock(SolarPanelRepository.class);
		SolarPanelApplicationService service = new SolarPanelApplicationService(repo);

		// WHEN
		SolarPanel solarPanel = new SolarPanel();
		service.save(solarPanel);

		// THEN
		verify(repo).save(solarPanel);
	}

	@Test
	void should_GetSolarPanelExpanded() {
		// GIVEN
		SolarPanelRepository repo = mock(SolarPanelRepository.class);
		SolarPanelApplicationService service = new SolarPanelApplicationService(repo);

		String serialNumber = "someSerialNumber";

		// WHEN
		service.getExpanded(serialNumber);

		// THEN
		verify(repo).findBySerialNumber(serialNumber);
	}

	@Test
	void should_ReturnGetSolarPanelExpanded() {
		// GIVEN
		SolarPanelRepository repo = mock(SolarPanelRepository.class);
		SolarPanelApplicationService service = new SolarPanelApplicationService(repo);

		String serialNumber = "someSerialNumber";
		GetSolarPanelExpanded expected = new GetSolarPanelExpanded();

		// WHEN
		GetSolarPanelExpanded actual = service.getExpanded(serialNumber);

		// THEN
		assertThat(actual).isEqualTo(expected);

	}

}
