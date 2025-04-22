package org.personal.solarpanel.dto;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import org.personal.solarpanel.enums.Recommendation;

@Builder(toBuilder = true)
public record SearchSolarPanelRequest(
		@Min(value = 0, message = "{NUMERIC_MINIMUM_VALUE}")
		int pageIndex,
		@Min(value = 1, message = "{NUMERIC_MINIMUM_VALUE}")
		int pageSize,

		String sortByField,
		String sortDirection,

		String manufacturer,
		String model,
		String type,
		String serialNumber,

		String createdAt,
		Recommendation recommendation
) { }
