package org.personal.washingmachine.mapper;

import org.personal.washingmachine.dto.GetWashingMachineFullResponse;
import org.personal.washingmachine.entity.WashingMachineImage;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public final class WashingMachineImageMapper {

	public List<GetWashingMachineFullResponse.Image> toGetWashingMachineImageResponses(List<WashingMachineImage> entities) {
		return entities.stream()
				.map(entity -> new GetWashingMachineFullResponse.Image(
						entity.getImagePrefix(),
						entity.getImage()))
				.toList();
	}

}
