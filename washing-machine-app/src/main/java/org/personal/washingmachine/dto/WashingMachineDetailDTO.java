package org.personal.washingmachine.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;

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
		@DecimalMin(value = "0", message = "MINIMUM_VALUE_0")
		@DecimalMax(value = "10", message = "MAXIMUM_VALUE_10")
        double visibleSurfacesScratchesLength,

        boolean visibleSurfacesHasDents,
		@DecimalMin(value = "0", message = "MINIMUM_VALUE_0")
		@DecimalMax(value = "10", message = "MAXIMUM_VALUE_10")
        double visibleSurfacesDentsDepth,

        boolean visibleSurfacesHasMinorDamage,
		@NotNull(message = "VALUE_NOT_NULL")
        String visibleSurfacesMinorDamage,

        boolean visibleSurfacesHasMajorDamage,
		@NotNull(message = "VALUE_NOT_NULL")
        String visibleSurfacesMajorDamage,

        // HIDDEN SURFACES
        boolean applicableHiddenSurfacesDamage,

        boolean hiddenSurfacesHasScratches,
		@DecimalMin(value = "0", message = "MINIMUM_VALUE_0")
		@DecimalMax(value = "10", message = "MAXIMUM_VALUE_10")
        double hiddenSurfacesScratchesLength,

        boolean hiddenSurfacesHasDents,
		@DecimalMin(value = "0", message = "MINIMUM_VALUE_0")
		@DecimalMax(value = "10", message = "MAXIMUM_VALUE_10")
        double hiddenSurfacesDentsDepth,

        boolean hiddenSurfacesHasMinorDamage,
		@NotNull(message = "VALUE_NOT_NULL")
        String hiddenSurfacesMinorDamage,

        boolean hiddenSurfacesHasMajorDamage,
		@NotNull(message = "VALUE_NOT_NULL")
        String hiddenSurfacesMajorDamage,

        // PRICING
        Integer price,
        Integer repairPrice
) {}
