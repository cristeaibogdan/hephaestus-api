package org.personal.washingmachine.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.ReturnType;

@Builder(toBuilder = true)
public record CreateWashingMachineRequest(
		@NotNull(message = "{FIELD_NOT_NULL}")
		String category,
		@NotNull(message = "{FIELD_NOT_NULL}")
		IdentificationMode identificationMode,
		@NotNull(message = "{FIELD_NOT_NULL}")
		String manufacturer,

		@NotNull(message = "{FIELD_NOT_NULL}")
		String model,
		@NotNull(message = "{FIELD_NOT_NULL}")
		String type,
		@NotNull(message = "{FIELD_NOT_NULL}")
		String serialNumber,

		@NotNull(message = "{FIELD_NOT_NULL}")
		ReturnType returnType,
		@NotNull(message = "{FIELD_NOT_NULL}")
		DamageType damageType,

		@Valid @NotNull(message = "{FIELD_NOT_NULL}")
		CreateWashingMachineDetailRequest createWashingMachineDetailRequest
) { }