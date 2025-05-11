package org.personal.washingmachine.mapper;

import lombok.RequiredArgsConstructor;
import org.personal.washingmachine.dto.GetWashingMachineDetailResponse;
import org.personal.washingmachine.dto.SaveWashingMachineRequest;
import org.personal.washingmachine.dto.GetWashingMachineFullResponse;
import org.personal.washingmachine.dto.GetWashingMachineSimpleResponse;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.entity.WashingMachineDetail;
import org.personal.washingmachine.entity.embedded.CostAssessment;
import org.personal.washingmachine.entity.embedded.HiddenSurfaceDamage;
import org.personal.washingmachine.entity.embedded.PackageDamage;
import org.personal.washingmachine.entity.embedded.VisibleSurfaceDamage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class WashingMachineMapper {
	private final WashingMachineImageMapper washingMachineImageMapper;

	public GetWashingMachineSimpleResponse toGetWashingMachineSimpleResponse(WashingMachine entity) {
		return new GetWashingMachineSimpleResponse(
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

				new GetWashingMachineDetailResponse(
						entity.getWashingMachineDetail().getPackageDamage().isApplicable(),
						entity.getWashingMachineDetail().getPackageDamage().isPackageDamaged(),
						entity.getWashingMachineDetail().getPackageDamage().isPackageDirty(),
						entity.getWashingMachineDetail().getPackageDamage().isPackageMaterialAvailable(),

						entity.getWashingMachineDetail().getVisibleSurfaceDamage().isApplicable(),
						entity.getWashingMachineDetail().getVisibleSurfaceDamage().hasScratches(),
						entity.getWashingMachineDetail().getVisibleSurfaceDamage().getVisibleSurfacesScratchesLength(),
						entity.getWashingMachineDetail().getVisibleSurfaceDamage().hasDents(),
						entity.getWashingMachineDetail().getVisibleSurfaceDamage().getVisibleSurfacesDentsDepth(),
						entity.getWashingMachineDetail().getVisibleSurfaceDamage().hasMinorDamage(),
						entity.getWashingMachineDetail().getVisibleSurfaceDamage().getVisibleSurfacesMinorDamage(),
						entity.getWashingMachineDetail().getVisibleSurfaceDamage().hasMajorDamage(),
						entity.getWashingMachineDetail().getVisibleSurfaceDamage().getVisibleSurfacesMajorDamage(),

						entity.getWashingMachineDetail().getHiddenSurfaceDamage().isApplicable(),
						entity.getWashingMachineDetail().getHiddenSurfaceDamage().hasScratches(),
						entity.getWashingMachineDetail().getHiddenSurfaceDamage().getHiddenSurfacesScratchesLength(),
						entity.getWashingMachineDetail().getHiddenSurfaceDamage().hasDents(),
						entity.getWashingMachineDetail().getHiddenSurfaceDamage().getHiddenSurfacesDentsDepth(),
						entity.getWashingMachineDetail().getHiddenSurfaceDamage().hasMinorDamage(),
						entity.getWashingMachineDetail().getHiddenSurfaceDamage().getHiddenSurfacesMinorDamage(),
						entity.getWashingMachineDetail().getHiddenSurfaceDamage().hasMajorDamage(),
						entity.getWashingMachineDetail().getHiddenSurfaceDamage().getHiddenSurfacesMajorDamage(),

						entity.getWashingMachineDetail().getCostAssessment().getPrice(),
						entity.getWashingMachineDetail().getCostAssessment().getRepairPrice()
				),
				washingMachineImageMapper.toGetWashingMachineImageResponses(entity.getWashingMachineImages())
		);
	}

	public WashingMachine toEntity(SaveWashingMachineRequest dto) {
		return new WashingMachine(
				dto.category(),
				dto.manufacturer(),
				dto.damageType(),
				dto.returnType(),
				dto.identificationMode(),
				dto.serialNumber(),
				dto.model(),
				dto.type(),
				new WashingMachineDetail(
						new PackageDamage(
								dto.washingMachineDetail().packageDamaged(),
								dto.washingMachineDetail().packageDirty(),
								dto.washingMachineDetail().packageMaterialAvailable()
						),
						new VisibleSurfaceDamage(
								dto.washingMachineDetail().visibleSurfacesScratchesLength(),
								dto.washingMachineDetail().visibleSurfacesDentsDepth(),
								dto.washingMachineDetail().visibleSurfacesMinorDamage(),
								dto.washingMachineDetail().visibleSurfacesMajorDamage()
						),
						new HiddenSurfaceDamage(
								dto.washingMachineDetail().hiddenSurfacesScratchesLength(),
								dto.washingMachineDetail().hiddenSurfacesDentsDepth(),
								dto.washingMachineDetail().hiddenSurfacesMinorDamage(),
								dto.washingMachineDetail().hiddenSurfacesMajorDamage()
						),
						new CostAssessment(
								dto.washingMachineDetail().price(),
								dto.washingMachineDetail().repairPrice()
						)
				)
		);
	}
}
