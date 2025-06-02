package org.personal.solarpanel.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.With;
import org.personal.solarpanel.enums.Recommendation;

@With
public record SearchSolarPanelRequest(
		@Min(value = 0, message = "{NUMERIC_MINIMUM_VALUE}")
		int pageIndex,
		@Min(value = 1, message = "{NUMERIC_MINIMUM_VALUE}")
		int pageSize,

		String sortByField,
		@Pattern(regexp = "^(asc|desc)?$", message = "Sort direction must be 'asc', 'desc', or empty")
		String sortDirection,

		String manufacturer,
		String model,
		String type,
		String serialNumber,

		String createdAt,
		Recommendation recommendation
) { }
