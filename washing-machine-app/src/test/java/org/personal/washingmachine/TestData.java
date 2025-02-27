package org.personal.washingmachine;

import org.personal.washingmachine.dto.CreateWashingMachineDetailRequest;
import org.personal.washingmachine.dto.CreateWashingMachineRequest;
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

	public static CreateWashingMachineDetailRequest createValidWashingMachineDetailRequest() {
		return new CreateWashingMachineDetailRequest(
				true,
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

	private static WashingMachineDetail createWashingMachineDetail() {
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

	public static WashingMachineDetail createWashingMachineDetailWithRecommendation(Recommendation expected) {
		return switch (expected) {
			case REPACKAGE -> createWashingMachineDetail()
					.setPackageDamage(
							new PackageDamage(
									true,
									false,
									true
							)
					);
			case RESALE -> createWashingMachineDetail()
					.setPackageDamage(
							new PackageDamage(
									true,
									false,
									false
							)
					);
			case OUTLET -> createWashingMachineDetail()
					.setVisibleSurfaceDamage(
							new VisibleSurfaceDamage(
									0,
									0,
									"",
									"Major Damage is present"
							)
					);
			case REPAIR -> createWashingMachineDetail()
					.setRepairPrice(100)
					.setPrice(500);
			case DISASSEMBLE -> createWashingMachineDetail()
					.setRepairPrice(400)
					.setPrice(500);
			case NONE -> createWashingMachineDetail();
		};
	}

	public static WashingMachine createValidWashingMachine(String serialNumber) {
		return new WashingMachine(
				"Washing Machine",
				"WhirlPool",
				DamageType.IN_USE,
				ReturnType.SERVICE,
				IdentificationMode.DATA_MATRIX,
				serialNumber,
				"modelOne",
				"typeOne",
				new WashingMachineDetail(
						new PackageDamage(
								true, // To avoid Invalid recommendation issued NONE
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
