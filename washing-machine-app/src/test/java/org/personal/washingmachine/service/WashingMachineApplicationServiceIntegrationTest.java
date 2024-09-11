package org.personal.washingmachine.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.personal.washingmachine.BaseIntegrationTest;
import org.personal.washingmachine.dto.WashingMachineDetailDTO;
import org.personal.washingmachine.dto.WashingMachineExpandedDTO;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.entity.WashingMachineDetail;
import org.personal.washingmachine.entity.embedded.HiddenSurfaceDamage;
import org.personal.washingmachine.entity.embedded.PackageDamage;
import org.personal.washingmachine.entity.embedded.VisibleSurfaceDamage;
import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.enums.ReturnType;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class WashingMachineApplicationServiceIntegrationTest extends BaseIntegrationTest {

	@Autowired
	private WashingMachineRepository repository;

	@Autowired
	private TestRestTemplate restTemplate;

	@Nested
	class testLoadExpanded {

		@Test
		void should_ReturnWashingMachineExpandedDTO() {
			// GIVEN
			WashingMachineDetail washingMachineDetail = new WashingMachineDetail(
					new PackageDamage(true, true, true),
					new VisibleSurfaceDamage(
							true,
							true,
							5.5,
							false,
							0,
							true,
							"some minor damage",
							true,
							"some major damage"
					),
					new HiddenSurfaceDamage(
							true,
							true,
							5.5,
							false,
							0,
							true,
							"some minor damage",
							true,
							"some major damage"
					),
					0,
					0
			);

			WashingMachine washingMachine = new WashingMachine(
					"Washing Machine",
					"Whirlpool",
					DamageType.IN_USE,
					ReturnType.SERVICE,
					IdentificationMode.DATA_MATRIX,
					"test",
					"modelOne",
					"typeOne",
					Recommendation.RESALE,
					washingMachineDetail
			);

			repository.save(washingMachine);

			WashingMachineExpandedDTO expected = new WashingMachineExpandedDTO(
					new WashingMachineDetailDTO(
							true,
							true,
							true,
							true,
							true,
							true,
							5.5,
							false,
							0,
							true,
							"some minor damage",
							true,
							"some major damage",
							true,
							true,
							5.5,
							false,
							0,
							true,
							"some minor damage",
							true,
							"some major damage",
							0,
							0
					),
					Collections.emptyList() // TODO: How to create test images?
			);

			// WHEN
			ResponseEntity<WashingMachineExpandedDTO> actual = restTemplate.exchange(
					"/api/v1/washing-machines/{serialNumber}/expanded",
					HttpMethod.GET,
					null,
					WashingMachineExpandedDTO.class,
					"test"
			);

			// THEN
			assertThat(actual.getStatusCode())
					.isEqualTo(HttpStatus.OK);

			assertThat(actual.getBody())
					.usingRecursiveComparison()
					.isEqualTo(expected);
		}
	}

}