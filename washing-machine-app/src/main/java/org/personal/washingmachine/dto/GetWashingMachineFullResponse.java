package org.personal.washingmachine.dto;

import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.enums.ReturnType;

import java.time.LocalDateTime;
import java.util.List;

public record GetWashingMachineFullResponse(
		String category,

		String manufacturer,
		IdentificationMode identificationMode,

		String model,
		String type,
		String serialNumber,

		ReturnType returnType,
		DamageType damageType,

		Recommendation recommendation,
		LocalDateTime createdAt,

        WashingMachineDetail washingMachineDetail,
        List<GetWashingMachineImageResponse> washingMachineImages
) {
	public record WashingMachineDetail(
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
			int price,
			int repairPrice
	) {}

	public record GetWashingMachineImageResponse(
			String imagePrefix,
			byte[] image
	) { }
}