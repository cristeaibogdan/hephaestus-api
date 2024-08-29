package org.personal.washingmachine.service.calculators;

import org.personal.washingmachine.dto.WashingMachineDetailsDTO;
import org.personal.washingmachine.enums.Recommendation;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

import static org.personal.washingmachine.enums.Recommendation.*;

@Component
public class HiddenSurfacesDamageCalculator implements ICalculator {

	private static final int HIDDEN_SURFACES_THRESHOLD = 7;

	@Override
	public Recommendation calculate(WashingMachineDetailsDTO dto) {
		if (!dto.applicableHiddenSurfacesDamage()) {
			return NONE;
		}

		Recommendation scratchesDamageLevel = calculateScratchesDamageLevel(dto);
		Recommendation dentsDamageLevel = calculateDentsDamageLevel(dto);
		Recommendation minorDamageLevel = calculateMinorDamageLevel(dto);
		Recommendation majorDamageLevel = calculateMajorDamageLevel(dto);

		return Collections.max(Arrays.asList(
				scratchesDamageLevel,
				dentsDamageLevel,
				minorDamageLevel,
				majorDamageLevel
		));
	}

	Recommendation calculateScratchesDamageLevel(WashingMachineDetailsDTO dto) {
		if (!dto.hiddenSurfacesHasScratches()) {
			return NONE;
		}

		return dto.hiddenSurfacesScratchesLength() < HIDDEN_SURFACES_THRESHOLD
				? RESALE
				: OUTLET;
	}

	Recommendation calculateDentsDamageLevel(WashingMachineDetailsDTO dto) {
		if (!dto.hiddenSurfacesHasDents()) {
			return NONE;
		}

		return dto.hiddenSurfacesDentsDepth() < HIDDEN_SURFACES_THRESHOLD
				? RESALE
				: OUTLET;
	}

	Recommendation calculateMinorDamageLevel(WashingMachineDetailsDTO dto) {
		return dto.hiddenSurfacesHasMinorDamage()
				? RESALE
				: NONE;
	}

	Recommendation calculateMajorDamageLevel(WashingMachineDetailsDTO dto) {
		return dto.hiddenSurfacesHasMajorDamage()
				? OUTLET
				: NONE;
	}
}
