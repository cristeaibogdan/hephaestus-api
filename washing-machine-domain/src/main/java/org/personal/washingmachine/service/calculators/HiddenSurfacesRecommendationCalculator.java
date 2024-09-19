package org.personal.washingmachine.service.calculators;

import com.google.common.annotations.VisibleForTesting;
import org.personal.washingmachine.entity.embedded.HiddenSurfaceDamage;
import org.personal.washingmachine.enums.Recommendation;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

import static org.personal.washingmachine.enums.Recommendation.*;

@Component
public class HiddenSurfacesRecommendationCalculator {

	private static final int HIDDEN_SURFACES_THRESHOLD = 7;

	public Recommendation calculate(HiddenSurfaceDamage detail) {
		if (!detail.isApplicable()) {
			return NONE;
		}

		Recommendation recommendationForScratches = calculateForScratches(detail);
		Recommendation recommendationForDents = calculateForDents(detail);
		Recommendation recommendationForMinorDamage = calculateForMinorDamage(detail);
		Recommendation recommendationForMajorDamage = calculateForMajorDamage(detail);

		return Collections.max(Arrays.asList(
				recommendationForScratches,
				recommendationForDents,
				recommendationForMinorDamage,
				recommendationForMajorDamage
		));
	}

	@VisibleForTesting
	Recommendation calculateForScratches(HiddenSurfaceDamage detail) {
		if (!detail.hasScratches()) {
			return NONE;
		}

		return detail.getHiddenSurfacesScratchesLength() < HIDDEN_SURFACES_THRESHOLD
				? RESALE
				: OUTLET;
	}

	@VisibleForTesting
	Recommendation calculateForDents(HiddenSurfaceDamage detail) {
		if (!detail.hasDents()) {
			return NONE;
		}

		return detail.getHiddenSurfacesDentsDepth() < HIDDEN_SURFACES_THRESHOLD
				? RESALE
				: OUTLET;
	}

	@VisibleForTesting
	Recommendation calculateForMinorDamage(HiddenSurfaceDamage detail) {
		return detail.hasMinorDamage()
				? RESALE
				: NONE;
	}

	@VisibleForTesting
	Recommendation calculateForMajorDamage(HiddenSurfaceDamage detail) {
		return detail.hasMajorDamage()
				? OUTLET
				: NONE;
	}
}
