package org.personal.washingmachine;

import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.enums.ReturnType;

public class WashingMachineTestData {

	public static WashingMachine getWashingMachineWithoutDetailAndImages() {
		return new WashingMachine(
				"Washing Machine",
				"Gorenje",
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
