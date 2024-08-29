package org.personal.washingmachine.service.calculators;

import org.personal.washingmachine.dto.WashingMachineDetailsDTO;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

import static org.personal.washingmachine.service.calculators.DamageLevel.*;

@Component
public class VisibleSurfacesDamageCalculator implements ICalculator {

	private static final int VISIBLE_SURFACES_THRESHOLD = 5;

	@Override
	public DamageLevel calculate(WashingMachineDetailsDTO dto) {
		if (!dto.applicableVisibleSurfacesDamage()) {
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
		if (!dto.visibleSurfacesHasScratches()) {
			return NONE;
		}

		return (dto.visibleSurfacesScratchesLength() < VISIBLE_SURFACES_THRESHOLD)
				? RESALE
				: OUTLET;
	}

	DamageLevel calculateDentsDamageLevel(WashingMachineDetailsDTO dto) {
		if (!dto.visibleSurfacesHasDents()) {
			return NONE;
		}

		return (dto.visibleSurfacesDentsDepth() < VISIBLE_SURFACES_THRESHOLD)
				? RESALE
				: OUTLET;
	}

	DamageLevel calculateMinorDamageLevel(WashingMachineDetailsDTO dto) {
		return dto.visibleSurfacesHasMinorDamage()
				? RESALE
				: NONE;
	}

	DamageLevel calculateMajorDamageLevel(WashingMachineDetailsDTO dto) {
		return (dto.visibleSurfacesHasMajorDamage())
				? OUTLET
				: NONE;
	}
}
