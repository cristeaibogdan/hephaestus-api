package org.personal.solarpanel.service;

import org.junit.jupiter.api.Test;
import org.personal.solarpanel.BaseIntegrationTest;
import org.personal.solarpanel.dto.SaveSolarPanelDamageRequest;
import org.personal.solarpanel.dto.SaveSolarPanelRequest;
import org.personal.solarpanel.entity.SolarPanel;
import org.personal.solarpanel.repository.SolarPanelRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class SaveIntegrationTest extends BaseIntegrationTest {

	@Autowired SolarPanelApplicationService underTest;
	@Autowired SolarPanelRepository repository;

	@Test
	void should_SaveRequest_With_AllPropertiesInDB() {
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
						false,
						"blue paint was thrown on half the panel"
				)
		);

		// WHEN
		underTest.save(request);
		SolarPanel actual = repository.findBySerialNumber("AX-098-200").orElseThrow();

		// THEN
		assertThat(actual).satisfies(act -> {
			// main fields
			assertThat(act.getCategory()).isEqualTo(request.category());
			assertThat(act.getManufacturer()).isEqualTo(request.manufacturer());
			assertThat(act.getModel()).isEqualTo(request.model());
			assertThat(act.getType()).isEqualTo(request.type());
			assertThat(act.getSerialNumber()).isEqualTo(request.serialNumber());

			// damage
			assertThat(act.getDamage().isHotSpots()).isEqualTo(request.saveSolarPanelDamageRequest().hotSpots());
			assertThat(act.getDamage().isMicroCracks()).isEqualTo(request.saveSolarPanelDamageRequest().microCracks());
			assertThat(act.getDamage().isSnailTrails()).isEqualTo(request.saveSolarPanelDamageRequest().snailTrails());
			assertThat(act.getDamage().isBrokenGlass()).isEqualTo(request.saveSolarPanelDamageRequest().brokenGlass());
			assertThat(act.getDamage().getAdditionalDetails()).isEqualTo(request.saveSolarPanelDamageRequest().additionalDetails());
		});
	}

	@Test
	void should_ReturnCurrentDate_For_createdAtField() {
		// GIVEN
		SaveSolarPanelRequest request = new SaveSolarPanelRequest(
				"Solar Panel",
				"Tesla",
				"model1",
				"type2",
				"date-test",
				new SaveSolarPanelDamageRequest(
						true,
						false,
						true,
						false,
						"blue paint was thrown on half the panel"
				)
		);

		// WHEN
		underTest.save(request);
		SolarPanel actual = repository.findBySerialNumber("date-test").orElseThrow();

		// THEN
		assertThat(actual.getCreatedAt().toLocalDate())
				.isEqualTo(LocalDate.now());
	}
}
