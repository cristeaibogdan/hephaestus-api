package org.personal.washingmachine.service.calculators;

import org.personal.washingmachine.dto.WashingMachineDetailDTO;
import org.personal.washingmachine.enums.Recommendation;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

import static org.personal.washingmachine.enums.Recommendation.*;

@Component
public class VisibleSurfacesRecommendationCalculator implements ICalculator {

	private static final int VISIBLE_SURFACES_THRESHOLD = 5;

	@Override
	public Recommendation calculate(WashingMachineDetailDTO dto) {
		if (!dto.applicableVisibleSurfacesDamage()) {
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

	Recommendation calculateForScratches(WashingMachineDetailDTO dto) {
		if (!dto.visibleSurfacesHasScratches()) {
			return NONE;
		}

		return (dto.visibleSurfacesScratchesLength() < VISIBLE_SURFACES_THRESHOLD)
				? RESALE
				: OUTLET;
	}

	Recommendation calculateForDents(WashingMachineDetailDTO dto) {
		if (!dto.visibleSurfacesHasDents()) {
			return NONE;
		}

		return (dto.visibleSurfacesDentsDepth() < VISIBLE_SURFACES_THRESHOLD)
				? RESALE
				: OUTLET;
	}

	Recommendation calculateForMinorDamage(WashingMachineDetailDTO dto) {
		return dto.visibleSurfacesHasMinorDamage()
				? RESALE
				: NONE;
	}

	Recommendation calculateForMajorDamage(WashingMachineDetailDTO dto) {
		return (dto.visibleSurfacesHasMajorDamage())
				? OUTLET
				: NONE;
	}
}
