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

	public static WashingMachineDetail createWashingMachineDetail() {
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

	public static WashingMachine createWashingMachine() {
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

	public static WashingMachine createWashingMachineWithManufacturer(String serialNumber, String manufacturer) {
		return new WashingMachine(
				"Washing Machine",
				manufacturer,
				DamageType.IN_USE,
				ReturnType.SERVICE,
				IdentificationMode.DATA_MATRIX,
				serialNumber,
				"modelOne",
				"typeOne",
				Recommendation.RESALE,
				null
		);
	}

	public static WashingMachine createWashingMachineWithDamageType(String serialNumber, DamageType damageType) {
		return new WashingMachine(
				"Washing Machine",
				"Bosch",
				damageType,
				ReturnType.SERVICE,
				IdentificationMode.DATA_MATRIX,
				serialNumber,
				"modelOne",
				"typeOne",
				Recommendation.RESALE,
				null
		);
	}

	public static WashingMachine createWashingMachineWithReturnType(String serialNumber, ReturnType returnType) {
		return new WashingMachine(
				"Washing Machine",
				"Bosch",
				DamageType.IN_USE,
				returnType,
				IdentificationMode.DATA_MATRIX,
				serialNumber,
				"modelOne",
				"typeOne",
				Recommendation.RESALE,
				null
		);
	}

	public static WashingMachine createWashingMachineWithIdentificationMode(String serialNumber, IdentificationMode identificationMode) {
		return new WashingMachine(
				"Washing Machine",
				"Bosch",
				DamageType.IN_USE,
				ReturnType.COMMERCIAL,
				identificationMode,
				serialNumber,
				"modelOne",
				"typeOne",
				Recommendation.RESALE,
				null
		);
	}

	public static WashingMachine createWashingMachineWithModel(String serialNumber, String model) {
		return new WashingMachine(
				"Washing Machine",
				"Bosch",
				DamageType.IN_USE,
				ReturnType.COMMERCIAL,
				IdentificationMode.DATA_MATRIX,
				serialNumber,
				model,
				"typeOne",
				Recommendation.RESALE,
				null
		);
	}

	public static WashingMachine createWashingMachineWithType(String serialNumber, String type) {  // TODO: Should all these methods be replaced with @Builder?
		return new WashingMachine(
				"Washing Machine",
				"Bosch",
				DamageType.IN_USE,
				ReturnType.COMMERCIAL,
				IdentificationMode.DATA_MATRIX,
				serialNumber,
				"modelOne",
				type,
				Recommendation.RESALE,
				null
		);
	}

	public static WashingMachine createWashingMachineWithRecommendation(String serialNumber, Recommendation recommendation) {
		return new WashingMachine(
				"Washing Machine",
				"Bosch",
				DamageType.IN_USE,
				ReturnType.COMMERCIAL,
				IdentificationMode.DATA_MATRIX,
				serialNumber,
				"modelOne",
				"TypeOne",
				recommendation,
				null
		);
	}

	public static WashingMachine createWashingMachineWithManufacturerAndReturnType(String serialNumber, String manufacturer, ReturnType returnType) {
		return new WashingMachine(
				"Washing Machine",
				manufacturer,
				DamageType.IN_USE,
				returnType,
				IdentificationMode.DATA_MATRIX,
				serialNumber,
				"modelOne",
				"typeOne",
				Recommendation.RESALE,
				null
		);
	}

	public static WashingMachine createWashingMachineWithIdentificationModeAndModelAndType(String serialNumber, IdentificationMode identificationMode, String model, String type) {
		return new WashingMachine(
				"Washing Machine",
				"Bosch",
				DamageType.IN_USE,
				ReturnType.SERVICE,
				identificationMode,
				serialNumber,
				model,
				type,
				Recommendation.RESALE,
				null
		);
	}
}
