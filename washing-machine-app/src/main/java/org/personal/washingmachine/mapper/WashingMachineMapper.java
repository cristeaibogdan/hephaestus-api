package org.personal.washingmachine.mapper;

import lombok.RequiredArgsConstructor;
import org.personal.washingmachine.dto.SearchWashingMachineResponse;
import org.personal.washingmachine.entity.WashingMachine;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class WashingMachineMapper {
	public SearchWashingMachineResponse toSearchWashingMachineResponse(WashingMachine entity) {
		return new SearchWashingMachineResponse(
				entity.getCategory(),
				entity.getManufacturer(),
				entity.getIdentificationMode(),
				entity.getModel(),
				entity.getType(),
				entity.getSerialNumber(),
				entity.getReturnType(),
				entity.getDamageType(),
				entity.getRecommendation(),
				entity.getCreatedAt()
		);
	}

}
