package org.personal.washingmachine.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.personal.shared.clients.ProductClient;
import org.personal.washingmachine.dto.GetWashingMachineFullResponse;
import org.personal.washingmachine.dto.Mapper;
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
import static org.personal.washingmachine.dto.Mapper.*;

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

	@Test
	void should_ReturnExpanded_When_ProvidedValidSerialNumber() {
		// GIVEN
		String serialNumber = "AXZ-900";
		WashingMachine washingMachine = new WashingMachine(
				"Washing Machine",
				"WhirlPool",
				DamageType.IN_USE,
				ReturnType.SERVICE,
				IdentificationMode.DATA_MATRIX,
				serialNumber,
				"model001",
				"type001",
				Recommendation.RESALE,
				new WashingMachineDetail(
						new PackageDamage(false, false, true),
						new VisibleSurfaceDamage(8, 4, "minor", "major"),
						new HiddenSurfaceDamage(6, 3, "minor", "major"),
						0,
						0
				)
		);
		given(washingMachineRepositoryMock.findBySerialNumber(serialNumber))
				.willReturn(Optional.of(washingMachine));

		GetWashingMachineFullResponse expected = WashingMachineMapper.toGetWashingMachineFullResponse(washingMachine);

		// WHEN
		GetWashingMachineFullResponse actual = underTest.load(serialNumber);

		// THEN
		assertThat(actual.washingMachineDetail())
				.usingRecursiveComparison()
				.isEqualTo(expected.washingMachineDetail());
	}

}