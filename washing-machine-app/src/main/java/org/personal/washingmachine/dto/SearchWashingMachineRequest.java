package org.personal.washingmachine.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.With;
import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.enums.ReturnType;

//@Builder(toBuilder = true) // Nostalgia for mutable objects.
@With // Prefer @With to @Builder, it's much cleaner
public record SearchWashingMachineRequest(
		@Min(value = 0, message = "{NUMERIC_MINIMUM_VALUE}")
		int pageIndex,
		@Min(value = 1, message = "{NUMERIC_MINIMUM_VALUE}")
		int pageSize,

		String sortByField,
		@Pattern(regexp = "^(asc|desc)?$", message = "Sort direction must be 'asc', 'desc', or empty")
		String sortDirection,

		IdentificationMode identificationMode,
		String manufacturer,

		String model,
		String type,
		String serialNumber,

		ReturnType returnType,
		DamageType damageType,

        Recommendation recommendation,

        String createdAt
) {}