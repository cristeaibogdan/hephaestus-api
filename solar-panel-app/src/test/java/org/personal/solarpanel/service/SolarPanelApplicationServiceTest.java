package org.personal.solarpanel.service;

import org.junit.jupiter.api.Test;
import org.personal.solarpanel.dto.SolarPanelDTO;
import org.personal.solarpanel.repository.SolarPanelRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SolarPanelApplicationServiceTest {

	SolarPanelRepository solarPanelRepository = mock(SolarPanelRepository.class);

	SolarPanelApplicationService solarPanelApplicationService = new SolarPanelApplicationService(solarPanelRepository);

	@Test
	void someMethod() {

		// GIVEN
		solarPanelApplicationService.save("Solar Panel", "Red", 100);

		// THEN

		// WHEN
		verify(solarPanelRepository).save("Solar Panel", "Red", 100);
	}

	@Test
	void should_GetOneInstance_When_ProvidedValidId() {
		// GIVEN
		Long id = 1L;

		SolarPanelDTO expected = new SolarPanelDTO("Solar Panel");

//		given(solarPanelRepository.findById(id))
//				.willReturn(expected);

		// WHEN
		SolarPanelDTO dto = solarPanelApplicationService.get(id);

		// THEN
		assertThat(dto)
				.isEqualTo(expected);
	}
}
