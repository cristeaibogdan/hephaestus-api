import org.junit.jupiter.api.Test;
import org.personal.solarpanel.dto.SolarPanelDTO;
import org.personal.solarpanel.repository.SolarPanelRepository;
import org.personal.solarpanel.service.SolarPanelApplication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SolarPanelApplicationTest {

	SolarPanelRepository solarPanelRepository = mock(SolarPanelRepository.class);

	SolarPanelApplication solarPanelApplication = new SolarPanelApplication(solarPanelRepository);

	@Test
	void someMethod() {

		// GIVEN
		solarPanelApplication.save("Solar Panel", "Red", 100);

		// THEN

		// WHEN
		verify(solarPanelRepository).save("Solar Panel", "Red", 100);
	}

	@Test
	void should_GetOneInstance_When_ProvidedValidId() {
		// GIVEN
		Long id = 1L;

		SolarPanelDTO expected = new SolarPanelDTO("Solar Panel");

		given(solarPanelRepository.findById(id))
				.willReturn(expected);

		// WHEN
		SolarPanelDTO dto = solarPanelApplication.get(id);

		// THEN
		assertThat(dto)
				.isEqualTo(expected);
	}
}
