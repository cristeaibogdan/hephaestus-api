package org.personal.washingmachine;

import org.personal.washingmachine.dto.SaveWashingMachineDetailRequest;
import org.personal.washingmachine.dto.SaveWashingMachineRequest;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.entity.WashingMachineDetail;
import org.personal.washingmachine.entity.embedded.CostAssessment;
import org.personal.washingmachine.entity.embedded.HiddenSurfaceDamage;
import org.personal.washingmachine.entity.embedded.PackageDamage;
import org.personal.washingmachine.entity.embedded.VisibleSurfaceDamage;
import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.enums.ReturnType;

public class TestData {

	//TODO: Refactor SaveValidationMvcTest and either delete this method or refactor it
	public static SaveWashingMachineDetailRequest createValidSaveWashingMachineDetailRequest() {
		return new SaveWashingMachineDetailRequest(
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

	public static SaveWashingMachineRequest createSaveWashingMachineRequest() {
		return new SaveWashingMachineRequest(
				"Washing Machine",
				IdentificationMode.DATA_MATRIX,
				"WhirlPool",
				"model100",
				"type200",
				"serialNumber",
				ReturnType.SERVICE,
				DamageType.IN_USE,
				new SaveWashingMachineDetailRequest(
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
				new CostAssessment(0, 0)
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
					.setCostAssessment(
							new CostAssessment(
								500,
								100
							)
					);
			case DISASSEMBLE -> createWashingMachineDetail()
					.setCostAssessment(
							new CostAssessment(
									500,
									400
							)
					);
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
						new CostAssessment(0, 0)
				)
		);
	}

}
