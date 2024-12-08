package org.personal.washingmachine.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.personal.washingmachine.BaseIntegrationTest;
import org.personal.washingmachine.TestData;
import org.personal.washingmachine.dto.GetWashingMachineReportResponse;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.enums.ReturnType;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional // Ensure Hibernate sessions are properly managed in your testing environment. Avoids "could not initialize proxy" Exception. Specific to tests only.
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GetReportIntegrationTest extends BaseIntegrationTest {

	// TODO: Had to put it in an integration test dues to createdAt property. Reflection might be another option, via ReflectionTestUtils
	@Autowired WashingMachineApplicationService underTest;
	@Autowired WashingMachineRepository washingMachineRepository;

	@BeforeAll
	void loadDataInDB() {
		washingMachineRepository.save(
				new WashingMachine("Washing Machine", "Gorenje", DamageType.IN_USE, ReturnType.COMMERCIAL, IdentificationMode.DATA_MATRIX, "I will return a Report!", "modelA", "TypeZ", Recommendation.OUTLET, TestData.washingMachineDetail())
		);
	}

	@AfterAll
	void cleanUpDB() {
		washingMachineRepository.deleteAll();
	}

	@Test
	void should_Count1ElementInDB() {
		assertThat(washingMachineRepository.count()).isEqualTo(1);
	}

	@Test
	void should_ReturnDTO_With_ValidProperties() {
		// GIVEN
		String serialNumber = "I will return a Report!";

		// WHEN
		GetWashingMachineReportResponse actual = underTest.getReport(serialNumber);

		// THEN
		assertThat(actual.report())
				.isNotNull()
				.isNotEmpty();

		assertThat(actual.createdAt())
				.isNotNull()
				.isNotEmpty();
	}

}
