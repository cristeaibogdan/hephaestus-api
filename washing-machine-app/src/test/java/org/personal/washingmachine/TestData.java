package org.personal.washingmachine;

import org.personal.washingmachine.dto.CreateWashingMachineRequest;
import org.personal.washingmachine.dto.SearchWashingMachineRequest;
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

	//TODO: Refactor CreateValidationMvcTest and either delete this method or refactor it
	public static CreateWashingMachineRequest.WashingMachineDetailRequest createValidWashingMachineDetailRequest() {
		return new CreateWashingMachineRequest.WashingMachineDetailRequest(
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

	public static CreateWashingMachineRequest createCreateWashingMachineRequest() {
		return new CreateWashingMachineRequest(
				"Washing Machine",
				IdentificationMode.DATA_MATRIX,
				"WhirlPool",
				"model100",
				"type200",
				"serialNumber",
				ReturnType.SERVICE,
				DamageType.IN_USE,
				new CreateWashingMachineRequest.WashingMachineDetailRequest(
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

	public static SearchWashingMachineRequest createSearchWashingMachineRequest() {
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
				null,
				null,
				null
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
