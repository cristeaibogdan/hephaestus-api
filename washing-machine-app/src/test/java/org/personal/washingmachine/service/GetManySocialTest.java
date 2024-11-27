package org.personal.washingmachine.service;

import org.junit.jupiter.api.Test;
import org.personal.shared.clients.ProductClient;
import org.personal.washingmachine.TestData;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.enums.ReturnType;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.personal.washingmachine.service.calculators.HiddenSurfacesRecommendationCalculator;
import org.personal.washingmachine.service.calculators.PackageRecommendationCalculator;
import org.personal.washingmachine.service.calculators.PricingRecommendationCalculator;
import org.personal.washingmachine.service.calculators.VisibleSurfacesRecommendationCalculator;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class GetManySocialTest {
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
	void should_callRepository_With_DistinctSerialNumbers() {
		// GIVEN
		List<String> serialNumbers = List.of(
				"serial1",
				"serial1",
				"serial2",
				"serial2",
				"serial3"
		);

		List<String> expected = List.of(
				"serial1",
				"serial2",
				"serial3"
		);

		List<WashingMachine> mockWashingMachines = new ArrayList<>(List.of(
				TestData.getWashingMachineWithoutImages()
		));

		given(washingMachineRepositoryMock.findAllBySerialNumberIn(anyList()))
				.willReturn(mockWashingMachines);

		// WHEN
		underTest.loadMany(serialNumbers);

		// THEN
		verify(washingMachineRepositoryMock).findAllBySerialNumberIn(expected);
	}
}
