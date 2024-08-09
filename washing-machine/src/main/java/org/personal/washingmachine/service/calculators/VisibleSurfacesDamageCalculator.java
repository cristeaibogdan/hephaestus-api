package org.personal.washingmachine.service.calculators;

import org.apache.commons.lang3.math.NumberUtils;
import org.personal.washingmachine.entity.dtos.WashingMachineDetailsDTO;
import org.springframework.stereotype.Component;

@Component
public class VisibleSurfacesDamageCalculator implements ICalculator {

	private static final int VISIBLE_SURFACES_THRESHOLD = 5;

	@Override
	public int calculate(WashingMachineDetailsDTO dto) {
		if (!dto.applicableVisibleSurfacesDamage()) {
			return 0;
		}

		int scratchesDamageLevel = calculateScratchesDamageLevel(dto);
		int dentsDamageLevel = calculateDentsDamageLevel(dto);
		int smallDamageLevel = calculateSmallDamageLevel(dto);
		int bigDamageLevel = calculateBigDamageLevel(dto);

		return NumberUtils.max(
				scratchesDamageLevel,
				dentsDamageLevel,
				smallDamageLevel,
				bigDamageLevel
		);
	}

	int calculateScratchesDamageLevel(WashingMachineDetailsDTO dto) {
		if (!dto.visibleSurfacesHasScratches()) {
			return 0;
		}

		return (dto.visibleSurfacesScratchesLength() < VISIBLE_SURFACES_THRESHOLD)
				? 2
				: 3;
	}

	int calculateDentsDamageLevel(WashingMachineDetailsDTO dto) {
		if (!dto.visibleSurfacesHasDents()) {
			return 0;
		}

		return (dto.visibleSurfacesDentsDepth() < VISIBLE_SURFACES_THRESHOLD)
				? 2
				: 3;
	}

	int calculateSmallDamageLevel(WashingMachineDetailsDTO dto) {
		return dto.visibleSurfacesHasMinorDamage()
				? 2
				: 0;
	}

	int calculateBigDamageLevel(WashingMachineDetailsDTO dto) {
		return (dto.visibleSurfacesHasMajorDamage())
				? 3
				: 0;
	}
}
