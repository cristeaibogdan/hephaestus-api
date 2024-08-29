package org.personal.washingmachine.service.calculators;

import org.personal.washingmachine.dto.WashingMachineDetailsDTO;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

import static org.personal.washingmachine.service.calculators.DamageLevel.*;

@Component
public class HiddenSurfacesDamageCalculator implements ICalculator {

	private static final int HIDDEN_SURFACES_THRESHOLD = 7;

	@Override
	public DamageLevel calculate(WashingMachineDetailsDTO dto) {
		if (!dto.applicableHiddenSurfacesDamage()) {
			return NONE;
		}

		DamageLevel scratchesDamageLevel = calculateScratchesDamageLevel(dto);
		DamageLevel dentsDamageLevel = calculateDentsDamageLevel(dto);
		DamageLevel minorDamageLevel = calculateMinorDamageLevel(dto);
		DamageLevel majorDamageLevel = calculateMajorDamageLevel(dto);

		return Collections.max(Arrays.asList(
				scratchesDamageLevel,
				dentsDamageLevel,
				minorDamageLevel,
				majorDamageLevel
		));
	}

	DamageLevel calculateScratchesDamageLevel(WashingMachineDetailsDTO dto) {
		if (!dto.hiddenSurfacesHasScratches()) {
			return NONE;
		}

		return dto.hiddenSurfacesScratchesLength() < HIDDEN_SURFACES_THRESHOLD
				? RESALE
				: OUTLET;
	}

	DamageLevel calculateDentsDamageLevel(WashingMachineDetailsDTO dto) {
		if (!dto.hiddenSurfacesHasDents()) {
			return NONE;
		}

		return dto.hiddenSurfacesDentsDepth() < HIDDEN_SURFACES_THRESHOLD
				? RESALE
				: OUTLET;
	}

	DamageLevel calculateMinorDamageLevel(WashingMachineDetailsDTO dto) {
		return dto.hiddenSurfacesHasMinorDamage()
				? RESALE
				: NONE;
	}

	DamageLevel calculateMajorDamageLevel(WashingMachineDetailsDTO dto) {
		return dto.hiddenSurfacesHasMajorDamage()
				? OUTLET
				: NONE;
	}
}
