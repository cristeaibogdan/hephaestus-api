package org.personal.washingmachine.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
		SaveWashingMachineDetailRequest saveWashingMachineDetailRequest
) { }