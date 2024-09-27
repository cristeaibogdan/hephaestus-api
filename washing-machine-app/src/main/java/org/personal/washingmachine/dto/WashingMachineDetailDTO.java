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
		@DecimalMin(value = "0", message = "{NUMERIC_MINIMUM_VALUE}")
		@DecimalMax(value = "10", message = "{NUMERIC_MAXIMUM_VALUE}")
        double visibleSurfacesScratchesLength,

        boolean visibleSurfacesHasDents,
		@DecimalMin(value = "0", message = "{NUMERIC_MINIMUM_VALUE}")
		@DecimalMax(value = "10", message = "{NUMERIC_MAXIMUM_VALUE}")
        double visibleSurfacesDentsDepth,

        boolean visibleSurfacesHasMinorDamage,
		@Size(max = 200, message = "{STRING_MAXIMUM_LENGTH}")
		@NotNull(message = "{FIELD_NOT_NULL}")
        String visibleSurfacesMinorDamage,

		boolean visibleSurfacesHasMajorDamage,
		@Size(max = 200, message = "{STRING_MAXIMUM_LENGTH}")
		@NotNull(message = "{FIELD_NOT_NULL}")
		String visibleSurfacesMajorDamage,

        // HIDDEN SURFACES
        boolean applicableHiddenSurfacesDamage,

        boolean hiddenSurfacesHasScratches,
		@DecimalMin(value = "0", message = "{NUMERIC_MINIMUM_VALUE}")
		@DecimalMax(value = "10", message = "{NUMERIC_MAXIMUM_VALUE}")
        double hiddenSurfacesScratchesLength,

        boolean hiddenSurfacesHasDents,
		@DecimalMin(value = "0", message = "{NUMERIC_MINIMUM_VALUE}")
		@DecimalMax(value = "10", message = "{NUMERIC_MAXIMUM_VALUE}")
        double hiddenSurfacesDentsDepth,

        boolean hiddenSurfacesHasMinorDamage,
		@Size(max = 200, message = "{STRING_MAXIMUM_LENGTH}")
		@NotNull(message = "{FIELD_NOT_NULL}")
        String hiddenSurfacesMinorDamage,

        boolean hiddenSurfacesHasMajorDamage,
		@Size(max = 200, message = "{STRING_MAXIMUM_LENGTH}")
		@NotNull(message = "{FIELD_NOT_NULL}")
        String hiddenSurfacesMajorDamage,

		// PRICING
		@PositiveOrZero(message = "{NUMERIC_VALUE_POSITIVE_OR_ZERO}")
		int price,
		@PositiveOrZero(message = "{NUMERIC_VALUE_POSITIVE_OR_ZERO}")
		int repairPrice
) {}
