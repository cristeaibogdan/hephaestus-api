package org.personal.washingmachine.service.calculators;

import org.personal.washingmachine.dto.WashingMachineDetailsDTO;
import org.personal.washingmachine.enums.Recommendation;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

import static org.personal.washingmachine.enums.Recommendation.*;

@Component
public class VisibleSurfacesDamageCalculator implements ICalculator {

	private static final int VISIBLE_SURFACES_THRESHOLD = 5;

	@Override
	public Recommendation calculate(WashingMachineDetailsDTO dto) {
		if (!dto.applicableVisibleSurfacesDamage()) {
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
		if (!dto.visibleSurfacesHasScratches()) {
			return NONE;
		}

		return (dto.visibleSurfacesScratchesLength() < VISIBLE_SURFACES_THRESHOLD)
				? RESALE
				: OUTLET;
	}

	Recommendation calculateDentsDamageLevel(WashingMachineDetailsDTO dto) {
		if (!dto.visibleSurfacesHasDents()) {
			return NONE;
		}

		return (dto.visibleSurfacesDentsDepth() < VISIBLE_SURFACES_THRESHOLD)
				? RESALE
				: OUTLET;
	}

	Recommendation calculateMinorDamageLevel(WashingMachineDetailsDTO dto) {
		return dto.visibleSurfacesHasMinorDamage()
				? RESALE
				: NONE;
	}

	Recommendation calculateMajorDamageLevel(WashingMachineDetailsDTO dto) {
		return (dto.visibleSurfacesHasMajorDamage())
				? OUTLET
				: NONE;
	}
}
