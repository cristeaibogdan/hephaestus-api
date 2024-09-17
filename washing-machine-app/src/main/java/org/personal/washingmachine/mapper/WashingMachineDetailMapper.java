package org.personal.washingmachine.mapper;

import org.personal.washingmachine.dto.WashingMachineDetailDTO;
import org.personal.washingmachine.entity.WashingMachineDetail;
import org.personal.washingmachine.entity.embedded.HiddenSurfaceDamage;
import org.personal.washingmachine.entity.embedded.PackageDamage;
import org.personal.washingmachine.entity.embedded.VisibleSurfaceDamage;
import org.springframework.stereotype.Component;

@Component
public class WashingMachineDetailMapper {

	public WashingMachineDetail toEntity(WashingMachineDetailDTO dto) {
		return new WashingMachineDetail(
				new PackageDamage(
						dto.packageDamaged(),
						dto.packageDirty(),
						dto.packageMaterialAvailable()
				),

				new VisibleSurfaceDamage(
						dto.visibleSurfacesHasScratches(),
						dto.visibleSurfacesScratchesLength(),
						dto.visibleSurfacesHasDents(),
						dto.visibleSurfacesDentsDepth(),
						dto.visibleSurfacesHasMinorDamage(),
						dto.visibleSurfacesMinorDamage(),
						dto.visibleSurfacesHasMajorDamage(),
						dto.visibleSurfacesMajorDamage()
				),

				new HiddenSurfaceDamage(
						dto.hiddenSurfacesHasScratches(),
						dto.hiddenSurfacesScratchesLength(),
						dto.hiddenSurfacesHasDents(),
						dto.hiddenSurfacesDentsDepth(),
						dto.hiddenSurfacesHasMinorDamage(),
						dto.hiddenSurfacesMinorDamage(),
						dto.hiddenSurfacesHasMajorDamage(),
						dto.hiddenSurfacesMajorDamage()
				),

				dto.price(),
				dto.repairPrice()
		);
	}

	public WashingMachineDetailDTO toDTO(WashingMachineDetail entity) {
		return new WashingMachineDetailDTO(
				entity.getPackageDamage().isApplicable(),
				entity.getPackageDamage().isPackageDamaged(),
				entity.getPackageDamage().isPackageDirty(),
				entity.getPackageDamage().isPackageMaterialAvailable(),

				entity.getVisibleSurfaceDamage().isApplicable(),
				entity.getVisibleSurfaceDamage().isVisibleSurfacesHasScratches(),
				entity.getVisibleSurfaceDamage().getVisibleSurfacesScratchesLength(),
				entity.getVisibleSurfaceDamage().isVisibleSurfacesHasDents(),
				entity.getVisibleSurfaceDamage().getVisibleSurfacesDentsDepth(),
				entity.getVisibleSurfaceDamage().isVisibleSurfacesHasMinorDamage(),
				entity.getVisibleSurfaceDamage().getVisibleSurfacesMinorDamage(),
				entity.getVisibleSurfaceDamage().isVisibleSurfacesHasMajorDamage(),
				entity.getVisibleSurfaceDamage().getVisibleSurfacesMajorDamage(),

				entity.getHiddenSurfaceDamage().isApplicable(),
				entity.getHiddenSurfaceDamage().isHiddenSurfacesHasScratches(),
				entity.getHiddenSurfaceDamage().getHiddenSurfacesScratchesLength(),
				entity.getHiddenSurfaceDamage().isHiddenSurfacesHasDents(),
				entity.getHiddenSurfaceDamage().getHiddenSurfacesDentsDepth(),
				entity.getHiddenSurfaceDamage().isHiddenSurfacesHasMinorDamage(),
				entity.getHiddenSurfaceDamage().getHiddenSurfacesMinorDamage(),
				entity.getHiddenSurfaceDamage().isHiddenSurfacesHasMajorDamage(),
				entity.getHiddenSurfaceDamage().getHiddenSurfacesMajorDamage(),

				entity.getPrice(),
				entity.getRepairPrice()
		);
	}
}
