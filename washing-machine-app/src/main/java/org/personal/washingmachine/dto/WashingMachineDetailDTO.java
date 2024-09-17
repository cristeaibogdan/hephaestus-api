package org.personal.washingmachine.dto;

import lombok.Builder;
import org.personal.washingmachine.entity.WashingMachineDetail;
import org.personal.washingmachine.entity.embedded.HiddenSurfaceDamage;
import org.personal.washingmachine.entity.embedded.PackageDamage;
import org.personal.washingmachine.entity.embedded.VisibleSurfaceDamage;

@Builder
public record WashingMachineDetailDTO(
        // PACKAGE
        boolean applicablePackageDamage,

        boolean packageDamaged,
        boolean packageDirty,
        boolean packageMaterialAvailable,

        // VISIBLE SURFACES
        boolean applicableVisibleSurfacesDamage,

        boolean visibleSurfacesHasScratches,
        double visibleSurfacesScratchesLength,

        boolean visibleSurfacesHasDents,
        double visibleSurfacesDentsDepth,

        boolean visibleSurfacesHasMinorDamage,
        String visibleSurfacesMinorDamage,

        boolean visibleSurfacesHasMajorDamage,
        String visibleSurfacesMajorDamage,

        // HIDDEN SURFACES
        boolean applicableHiddenSurfacesDamage,

        boolean hiddenSurfacesHasScratches,
        double hiddenSurfacesScratchesLength,

        boolean hiddenSurfacesHasDents,
        double hiddenSurfacesDentsDepth,

        boolean hiddenSurfacesHasMinorDamage,
        String hiddenSurfacesMinorDamage,

        boolean hiddenSurfacesHasMajorDamage,
        String hiddenSurfacesMajorDamage,

        // PRICING
        Integer price,
        Integer repairPrice
) {

	public WashingMachineDetailDTO(WashingMachineDetail entity) {
		this(
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

	public WashingMachineDetail toEntity() {
		return new WashingMachineDetail(
				new PackageDamage(
						this.packageDamaged,
						this.packageDirty,
						this.packageMaterialAvailable
				),

				new VisibleSurfaceDamage(
						this.visibleSurfacesHasScratches,
						this.visibleSurfacesScratchesLength,
						this.visibleSurfacesHasDents,
						this.visibleSurfacesDentsDepth,
						this.visibleSurfacesHasMinorDamage,
						this.visibleSurfacesMinorDamage,
						this.visibleSurfacesHasMajorDamage,
						this.visibleSurfacesMajorDamage
				),

				new HiddenSurfaceDamage(
						this.hiddenSurfacesHasScratches,
						this.hiddenSurfacesScratchesLength,
						this.hiddenSurfacesHasDents,
						this.hiddenSurfacesDentsDepth,
						this.hiddenSurfacesHasMinorDamage,
						this.hiddenSurfacesMinorDamage,
						this.hiddenSurfacesHasMajorDamage,
						this.hiddenSurfacesMajorDamage
				),

				this.price,
				this.repairPrice
		);
	}
}
