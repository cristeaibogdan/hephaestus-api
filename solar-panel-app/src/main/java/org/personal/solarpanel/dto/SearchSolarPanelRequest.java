package org.personal.solarpanel.dto;

import lombok.Builder;
import org.personal.solarpanel.enums.Recommendation;

@Builder(toBuilder = true)
public record SearchSolarPanelRequest(
		int pageIndex,
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
