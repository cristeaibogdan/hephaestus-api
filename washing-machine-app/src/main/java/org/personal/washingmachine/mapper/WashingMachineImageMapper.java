package org.personal.washingmachine.mapper;

import org.personal.washingmachine.dto.WashingMachineImageDTO;
import org.personal.washingmachine.entity.WashingMachineImage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WashingMachineImageMapper {
	public List<WashingMachineImageDTO> toDTO(List<WashingMachineImage> entities) {
		return entities.stream()
				.map(entity -> new WashingMachineImageDTO(
						entity.getImagePrefix(),
						entity.getImage()))
				.toList();
	}
}