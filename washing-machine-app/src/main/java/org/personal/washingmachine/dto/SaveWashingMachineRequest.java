package org.personal.washingmachine.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.ReturnType;

@Builder(toBuilder = true)
public record SaveWashingMachineRequest(
		@NotBlank(message = "{FIELD_NOT_BLANK}")
		String category,
		@NotNull(message = "{FIELD_NOT_NULL}")
		IdentificationMode identificationMode,
		@NotBlank(message = "{FIELD_NOT_BLANK}")
		String manufacturer,

		@NotBlank(message = "{FIELD_NOT_BLANK}")
		String model,
		@NotBlank(message = "{FIELD_NOT_BLANK}")
		String type,
		@NotBlank(message = "{FIELD_NOT_BLANK}")
		String serialNumber,

		@NotNull(message = "{FIELD_NOT_NULL}")
		ReturnType returnType,
		@NotNull(message = "{FIELD_NOT_NULL}")
		DamageType damageType,

		@Valid @NotNull(message = "{FIELD_NOT_NULL}")
		WashingMachineDetail washingMachineDetail
) {
	@Builder(toBuilder = true)
	public record WashingMachineDetail(
			// PACKAGE
			boolean packageDamaged,
			boolean packageDirty,
			boolean packageMaterialAvailable,

			// VISIBLE SURFACES
			@DecimalMin(value = "0", message = "{NUMERIC_MINIMUM_VALUE}")
			@DecimalMax(value = "10", message = "{NUMERIC_MAXIMUM_VALUE}")
			double visibleSurfacesScratchesLength,

			@DecimalMin(value = "0", message = "{NUMERIC_MINIMUM_VALUE}")
			@DecimalMax(value = "10", message = "{NUMERIC_MAXIMUM_VALUE}")
			double visibleSurfacesDentsDepth,

			@Size(max = 200, message = "{STRING_MAXIMUM_LENGTH}")
			@NotNull(message = "{FIELD_NOT_NULL}")
			String visibleSurfacesMinorDamage,

			@Size(max = 200, message = "{STRING_MAXIMUM_LENGTH}")
			@NotNull(message = "{FIELD_NOT_NULL}")
			String visibleSurfacesMajorDamage,

			// HIDDEN SURFACES
			@DecimalMin(value = "0", message = "{NUMERIC_MINIMUM_VALUE}")
			@DecimalMax(value = "10", message = "{NUMERIC_MAXIMUM_VALUE}")
			double hiddenSurfacesScratchesLength,

			@DecimalMin(value = "0", message = "{NUMERIC_MINIMUM_VALUE}")
			@DecimalMax(value = "10", message = "{NUMERIC_MAXIMUM_VALUE}")
			double hiddenSurfacesDentsDepth,

			@Size(max = 200, message = "{STRING_MAXIMUM_LENGTH}")
			@NotNull(message = "{FIELD_NOT_NULL}")
			String hiddenSurfacesMinorDamage,

			@Size(max = 200, message = "{STRING_MAXIMUM_LENGTH}")
			@NotNull(message = "{FIELD_NOT_NULL}")
			String hiddenSurfacesMajorDamage,

			// PRICING
			@PositiveOrZero(message = "{NUMERIC_VALUE_POSITIVE_OR_ZERO}")
			int price,
			@PositiveOrZero(message = "{NUMERIC_VALUE_POSITIVE_OR_ZERO}")
			int repairPrice
	) {}
}