package org.personal.washingmachine.dto;

import org.personal.washingmachine.entity.WashingMachineDetail;
import org.personal.washingmachine.entity.embedded.HiddenSurfaceDamage;
import org.personal.washingmachine.entity.embedded.PackageDamage;
import org.personal.washingmachine.entity.embedded.VisibleSurfaceDamage;
import org.springframework.stereotype.Component;

@Component
public final class WashingMachineDetailMapper {

	public GetWashingMachineDetailResponse toGetWashingMachineDetailResponse(WashingMachineDetail entity) {
		return new GetWashingMachineDetailResponse(
				entity.getPackageDamage().isApplicable(),
				entity.getPackageDamage().isPackageDamaged(),
				entity.getPackageDamage().isPackageDirty(),
				entity.getPackageDamage().isPackageMaterialAvailable(),

				entity.getVisibleSurfaceDamage().isApplicable(),
				entity.getVisibleSurfaceDamage().hasScratches(),
				entity.getVisibleSurfaceDamage().getVisibleSurfacesScratchesLength(),
				entity.getVisibleSurfaceDamage().hasDents(),
				entity.getVisibleSurfaceDamage().getVisibleSurfacesDentsDepth(),
				entity.getVisibleSurfaceDamage().hasMinorDamage(),
				entity.getVisibleSurfaceDamage().getVisibleSurfacesMinorDamage(),
				entity.getVisibleSurfaceDamage().hasMajorDamage(),
				entity.getVisibleSurfaceDamage().getVisibleSurfacesMajorDamage(),

				entity.getHiddenSurfaceDamage().isApplicable(),
				entity.getHiddenSurfaceDamage().hasScratches(),
				entity.getHiddenSurfaceDamage().getHiddenSurfacesScratchesLength(),
				entity.getHiddenSurfaceDamage().hasDents(),
				entity.getHiddenSurfaceDamage().getHiddenSurfacesDentsDepth(),
				entity.getHiddenSurfaceDamage().hasMinorDamage(),
				entity.getHiddenSurfaceDamage().getHiddenSurfacesMinorDamage(),
				entity.getHiddenSurfaceDamage().hasMajorDamage(),
				entity.getHiddenSurfaceDamage().getHiddenSurfacesMajorDamage(),

				entity.getPrice(),
				entity.getRepairPrice()
		);
	}

	public WashingMachineDetail toEntity(CreateWashingMachineDetailRequest dto) {
		return new WashingMachineDetail(
				new PackageDamage(
						dto.packageDamaged(),
						dto.packageDirty(),
						dto.packageMaterialAvailable()
				),
				new VisibleSurfaceDamage(
						dto.visibleSurfacesScratchesLength(),
						dto.visibleSurfacesDentsDepth(),
						dto.visibleSurfacesMinorDamage(),
						dto.visibleSurfacesMajorDamage()
				),
				new HiddenSurfaceDamage(
						dto.hiddenSurfacesScratchesLength(),
						dto.hiddenSurfacesDentsDepth(),
						dto.hiddenSurfacesMinorDamage(),
						dto.hiddenSurfacesMajorDamage()
				),
				dto.price(),
				dto.repairPrice()
		);
	}
}