package org.personal.washingmachine.mapper;

import lombok.RequiredArgsConstructor;
import org.personal.washingmachine.dto.CreateWashingMachineRequest;
import org.personal.washingmachine.dto.GetWashingMachineFullResponse;
import org.personal.washingmachine.dto.SearchWashingMachineResponse;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.entity.WashingMachineDamage;
import org.personal.washingmachine.entity.embedded.CostAssessment;
import org.personal.washingmachine.entity.embedded.HiddenSurfaceDamage;
import org.personal.washingmachine.entity.embedded.PackageDamage;
import org.personal.washingmachine.entity.embedded.VisibleSurfaceDamage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class WashingMachineMapper {

	private final WashingMachineImageMapper washingMachineImageMapper;

	public GetWashingMachineFullResponse toGetWashingMachineFullResponse(WashingMachine entity) {
		return new GetWashingMachineFullResponse(
				entity.getCategory(),
				entity.getManufacturer(),
				entity.getIdentificationMode(),
				entity.getModel(),
				entity.getType(),
				entity.getSerialNumber(),
				entity.getReturnType(),
				entity.getDamageType(),
				entity.getRecommendation(),
				entity.getCreatedAt(),

				new GetWashingMachineFullResponse.WashingMachineDamage(
						entity.getWashingMachineDamage().getPackageDamage().isApplicable(),
						entity.getWashingMachineDamage().getPackageDamage().isPackageDamaged(),
						entity.getWashingMachineDamage().getPackageDamage().isPackageDirty(),
						entity.getWashingMachineDamage().getPackageDamage().isPackageMaterialAvailable(),

						entity.getWashingMachineDamage().getVisibleSurfaceDamage().isApplicable(),
						entity.getWashingMachineDamage().getVisibleSurfaceDamage().hasScratches(),
						entity.getWashingMachineDamage().getVisibleSurfaceDamage().getVisibleSurfacesScratchesLength(),
						entity.getWashingMachineDamage().getVisibleSurfaceDamage().hasDents(),
						entity.getWashingMachineDamage().getVisibleSurfaceDamage().getVisibleSurfacesDentsDepth(),
						entity.getWashingMachineDamage().getVisibleSurfaceDamage().hasMinorDamage(),
						entity.getWashingMachineDamage().getVisibleSurfaceDamage().getVisibleSurfacesMinorDamage(),
						entity.getWashingMachineDamage().getVisibleSurfaceDamage().hasMajorDamage(),
						entity.getWashingMachineDamage().getVisibleSurfaceDamage().getVisibleSurfacesMajorDamage(),

						entity.getWashingMachineDamage().getHiddenSurfaceDamage().isApplicable(),
						entity.getWashingMachineDamage().getHiddenSurfaceDamage().hasScratches(),
						entity.getWashingMachineDamage().getHiddenSurfaceDamage().getHiddenSurfacesScratchesLength(),
						entity.getWashingMachineDamage().getHiddenSurfaceDamage().hasDents(),
						entity.getWashingMachineDamage().getHiddenSurfaceDamage().getHiddenSurfacesDentsDepth(),
						entity.getWashingMachineDamage().getHiddenSurfaceDamage().hasMinorDamage(),
						entity.getWashingMachineDamage().getHiddenSurfaceDamage().getHiddenSurfacesMinorDamage(),
						entity.getWashingMachineDamage().getHiddenSurfaceDamage().hasMajorDamage(),
						entity.getWashingMachineDamage().getHiddenSurfaceDamage().getHiddenSurfacesMajorDamage(),

						entity.getWashingMachineDamage().getCostAssessment().getPrice(),
						entity.getWashingMachineDamage().getCostAssessment().getRepairPrice()
				),
				washingMachineImageMapper.toGetWashingMachineImageResponses(entity.getWashingMachineImages())
		);
	}

	public SearchWashingMachineResponse toSearchWashingMachineResponse(WashingMachine entity) {
		return new SearchWashingMachineResponse(
				entity.getCategory(),
				entity.getManufacturer(),
				entity.getIdentificationMode(),
				entity.getModel(),
				entity.getType(),
				entity.getSerialNumber(),
				entity.getReturnType(),
				entity.getDamageType(),
				entity.getRecommendation(),
				entity.getCreatedAt()
		);
	}

	public WashingMachine toEntity(CreateWashingMachineRequest dto) {
		return new WashingMachine(
				dto.category(),
				dto.manufacturer(),
				dto.damageType(),
				dto.returnType(),
				dto.identificationMode(),
				dto.serialNumber(),
				dto.model(),
				dto.type(),
				new WashingMachineDamage(
						new PackageDamage(
								dto.washingMachineDamage().packageDamaged(),
								dto.washingMachineDamage().packageDirty(),
								dto.washingMachineDamage().packageMaterialAvailable()
						),
						new VisibleSurfaceDamage(
								dto.washingMachineDamage().visibleSurfacesScratchesLength(),
								dto.washingMachineDamage().visibleSurfacesDentsDepth(),
								dto.washingMachineDamage().visibleSurfacesMinorDamage(),
								dto.washingMachineDamage().visibleSurfacesMajorDamage()
						),
						new HiddenSurfaceDamage(
								dto.washingMachineDamage().hiddenSurfacesScratchesLength(),
								dto.washingMachineDamage().hiddenSurfacesDentsDepth(),
								dto.washingMachineDamage().hiddenSurfacesMinorDamage(),
								dto.washingMachineDamage().hiddenSurfacesMajorDamage()
						),
						new CostAssessment(
								dto.washingMachineDamage().price(),
								dto.washingMachineDamage().repairPrice()
						)
				)
		);
	}
}
