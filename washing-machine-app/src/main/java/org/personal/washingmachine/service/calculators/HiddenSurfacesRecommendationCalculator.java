package org.personal.washingmachine.service.calculators;

import org.personal.washingmachine.dto.WashingMachineDetailsDTO;
import org.personal.washingmachine.enums.Recommendation;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

import static org.personal.washingmachine.enums.Recommendation.*;

@Component
public class HiddenSurfacesRecommendationCalculator implements ICalculator {

	private static final int HIDDEN_SURFACES_THRESHOLD = 7;

	@Override
	public Recommendation calculate(WashingMachineDetailsDTO dto) {
		if (!dto.applicableHiddenSurfacesDamage()) {
			return NONE;
		}

		Recommendation recommendationForScratches = calculateForScratches(dto);
		Recommendation recommendationForDents = calculateForDents(dto);
		Recommendation recommendationForMinorDamage = calculateForMinorDamage(dto);
		Recommendation recommendationForMajorDamage = calculateForMajorDamage(dto);

		return Collections.max(Arrays.asList(
				recommendationForScratches,
				recommendationForDents,
				recommendationForMinorDamage,
				recommendationForMajorDamage
		));
	}

	Recommendation calculateForScratches(WashingMachineDetailsDTO dto) {
		if (!dto.hiddenSurfacesHasScratches()) {
			return NONE;
		}

		return dto.hiddenSurfacesScratchesLength() < HIDDEN_SURFACES_THRESHOLD
				? RESALE
				: OUTLET;
	}

	Recommendation calculateForDents(WashingMachineDetailsDTO dto) {
		if (!dto.hiddenSurfacesHasDents()) {
			return NONE;
		}

		return dto.hiddenSurfacesDentsDepth() < HIDDEN_SURFACES_THRESHOLD
				? RESALE
				: OUTLET;
	}

	Recommendation calculateForMinorDamage(WashingMachineDetailsDTO dto) {
		return dto.hiddenSurfacesHasMinorDamage()
				? RESALE
				: NONE;
	}

	Recommendation calculateForMajorDamage(WashingMachineDetailsDTO dto) {
		return dto.hiddenSurfacesHasMajorDamage()
				? OUTLET
				: NONE;
	}
}
