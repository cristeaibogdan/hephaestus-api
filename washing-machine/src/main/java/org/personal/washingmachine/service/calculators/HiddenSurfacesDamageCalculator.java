package org.personal.washingmachine.service.calculators;

import org.apache.commons.lang3.math.NumberUtils;
import org.personal.washingmachine.entity.dtos.WashingMachineDetailsDTO;
import org.springframework.stereotype.Component;

@Component
public class HiddenSurfacesDamageCalculator implements ICalculator {

	private static final int HIDDEN_SURFACES_THRESHOLD = 7;

	@Override
	public int calculate(WashingMachineDetailsDTO dto) {
		if (!dto.applicableHiddenSurfacesDamage()) {
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
				bigDamageLevel);
	}

	int calculateScratchesDamageLevel(WashingMachineDetailsDTO dto) {
		if (!dto.hiddenSurfacesHasScratches()) {
			return 0;
		}

		return dto.hiddenSurfacesScratchesLength() < HIDDEN_SURFACES_THRESHOLD
				? 2
				: 3;
	}

	int calculateDentsDamageLevel(WashingMachineDetailsDTO dto) {
		if (!dto.hiddenSurfacesHasDents()) {
			return 0;
		}

		return dto.hiddenSurfacesDentsDepth() < HIDDEN_SURFACES_THRESHOLD
				? 2
				: 3;
	}

	int calculateSmallDamageLevel(WashingMachineDetailsDTO dto) {
		return dto.hiddenSurfacesHasSmallDamage()
				? 2
				: 0;
	}

	int calculateBigDamageLevel(WashingMachineDetailsDTO dto) {
		return dto.hiddenSurfacesHasBigDamage()
				? 3
				: 0;
	}
}
