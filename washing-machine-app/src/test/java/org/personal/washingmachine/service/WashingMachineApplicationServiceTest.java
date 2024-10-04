package org.personal.washingmachine.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.personal.shared.clients.ProductClient;
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
import org.personal.washingmachine.service.calculators.HiddenSurfacesRecommendationCalculator;
import org.personal.washingmachine.service.calculators.PackageRecommendationCalculator;
import org.personal.washingmachine.service.calculators.PricingRecommendationCalculator;
import org.personal.washingmachine.service.calculators.VisibleSurfacesRecommendationCalculator;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class WashingMachineApplicationServiceTest {
	WashingMachineRepository washingMachineRepositoryMock = mock(WashingMachineRepository.class);

	WashingMachineService washingMachineService = new WashingMachineService(washingMachineRepositoryMock);
	WashingMachineDamageCalculator washingMachineDamageCalculator = new WashingMachineDamageCalculator(
			new PackageRecommendationCalculator(),
			new VisibleSurfacesRecommendationCalculator(),
			new HiddenSurfacesRecommendationCalculator(),
			new PricingRecommendationCalculator()
	);
	WashingMachineReportGenerator washingMachineReportGenerator = new WashingMachineReportGenerator();

	ProductClient productClient; //TODO: To be deleted

	WashingMachineApplicationService underTest = new WashingMachineApplicationService(
			washingMachineService,
			washingMachineRepositoryMock,
			washingMachineDamageCalculator,
			washingMachineReportGenerator,
			productClient
	);

	@Nested
	class testGetReport {

		@Test
		void should_ReturnReport() {
			// GIVEN
			String serialNumber = "ABC-987";

			WashingMachineDetail washingMachineDetail = new WashingMachineDetail(
					new PackageDamage(true, true, true),
					new VisibleSurfaceDamage(
							5.5,
							0,
							"some minor damage",
							"some major damage"
					),
					new HiddenSurfaceDamage(
							5.5,
							0,
							"some minor damage",
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

			// washingMachine.addImage(); TODO: How to create images?

			given(washingMachineRepositoryMock.findBySerialNumber(serialNumber))
					.willReturn(Optional.of(washingMachine));

			// WHEN
//            WashingMachineReportDTO report = underTest.getReport(serialNumber);

			// THEN
//            assertThat(report.createdAt())
//                    .isEqualTo(LocalDateTime.now()); TODO: This is not possible, as hibernate generated the date after saving...
		}
	}

	@Nested
	class testIsSerialNumberInUse {

		@ParameterizedTest
		@ValueSource(booleans = {true, false})
		void should_ReturnTrue(boolean expected) {
			// GIVEN
			String serialNumber = "ABC-987";

			given(washingMachineRepositoryMock.existsBySerialNumber(serialNumber))
					.willReturn(expected);

			// WHEN
			boolean actual = underTest.isSerialNumberInUse(serialNumber);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}

}