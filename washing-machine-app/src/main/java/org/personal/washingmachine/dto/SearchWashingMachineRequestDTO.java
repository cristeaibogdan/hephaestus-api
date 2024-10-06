package org.personal.washingmachine.dto;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.enums.ReturnType;

@Builder(toBuilder = true)
public record SearchWashingMachineRequestDTO(
		@Min(value = 0, message = "{NUMERIC_MINIMUM_VALUE}")
		int pageIndex,
		@Min(value = 1, message = "{NUMERIC_MINIMUM_VALUE}")
		int pageSize,

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