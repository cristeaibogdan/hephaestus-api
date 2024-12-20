package org.personal.washingmachine;

import org.personal.washingmachine.dto.CreateWashingMachineDetailRequest;
import org.personal.washingmachine.dto.CreateWashingMachineRequest;
import org.personal.washingmachine.dto.SearchWashingMachineRequest;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.entity.WashingMachineDetail;
import org.personal.washingmachine.entity.embedded.HiddenSurfaceDamage;
import org.personal.washingmachine.entity.embedded.PackageDamage;
import org.personal.washingmachine.entity.embedded.VisibleSurfaceDamage;
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

	public static WashingMachineDetail washingMachineDetail() {
		return new WashingMachineDetail(
				new PackageDamage(
						false,
						false,
						false),
				new VisibleSurfaceDamage(
						0,
						0,
						"",
						""),
				new HiddenSurfaceDamage(
						0,
						0,
						"",
						""),
				0,
				0
		);
	}

	public static WashingMachine getWashingMachineWithoutImages() {
		return new WashingMachine(
				"Washing Machine",
				"WhirlPool",
				DamageType.IN_USE,
				ReturnType.SERVICE,
				IdentificationMode.DATA_MATRIX,
				"test",
				"modelOne",
				"typeOne",
				Recommendation.RESALE,
				new WashingMachineDetail(
						new PackageDamage(
								false,
								false,
								false),
						new VisibleSurfaceDamage(
								0,
								0,
								"",
								""),
						new HiddenSurfaceDamage(
								0,
								0,
								"",
								""),
						0,
						0
				)
		);
	}

	public static WashingMachine createWashingMachineWithSerialNumber(String serialNumber) {
		return new WashingMachine(
				"Washing Machine",
				"Whirlpool",
				DamageType.IN_USE,
				ReturnType.SERVICE,
				IdentificationMode.DATA_MATRIX,
				serialNumber,
				"modelOne",
				"typeOne",
				Recommendation.RESALE,
				new WashingMachineDetail(
						new PackageDamage(
								false,
								false,
								false),
						new VisibleSurfaceDamage(
								0,
								0,
								"",
								""),
						new HiddenSurfaceDamage(
								0,
								0,
								"",
								""),
						0,
						0
				)
		);
	}
}
