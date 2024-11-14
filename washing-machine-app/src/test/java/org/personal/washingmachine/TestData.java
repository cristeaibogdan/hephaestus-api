package org.personal.washingmachine;

import org.personal.washingmachine.dto.CreateWashingMachineDetailRequest;
import org.personal.washingmachine.dto.CreateWashingMachineRequest;
import org.personal.washingmachine.dto.SearchWashingMachineRequest;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.enums.ReturnType;

public class TestData {

	public static SearchWashingMachineRequest searchWashingMachineRequest() {
		return new SearchWashingMachineRequest(
				0,
				2,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null
		);
	}

	public static CreateWashingMachineDetailRequest createWashingMachineDetailRequest() {
		return new CreateWashingMachineDetailRequest(
						false,
						false,
						false,
						0,
						0,
						"",
						"",
						0,
						0,
						"",
						"",
						0,
						0
				);
	}

	public static CreateWashingMachineRequest createWashingMachineRequest() {
		return new CreateWashingMachineRequest(
				"Washing Machine",
				IdentificationMode.DATA_MATRIX,
				"WhirlPool",
				"model100",
				"type200",
				"serialNumber",
				ReturnType.SERVICE,
				DamageType.IN_USE,
				new CreateWashingMachineDetailRequest(
						false,
						false,
						false,
						0,
						0,
						"",
						"",
						0,
						0,
						"",
						"",
						0,
						0
				)
		);
	}

	public static WashingMachine getWashingMachineWithoutDetailAndImages(String manufacturer) {
		return new WashingMachine(
				"Washing Machine",
				manufacturer,
				DamageType.IN_USE,
				ReturnType.SERVICE,
				IdentificationMode.DATA_MATRIX,
				"test",
				"modelOne",
				"typeOne",
				Recommendation.RESALE,
				null
		);
	}

	public static WashingMachine getWashingMachineWithoutDetailAndImages(IdentificationMode identificationMode) {
		return new WashingMachine(
				"Washing Machine",
				"Whirlpool",
				DamageType.IN_USE,
				ReturnType.SERVICE,
				identificationMode,
				"test",
				"modelOne",
				"typeOne",
				Recommendation.RESALE,
				null
		);
	}
}