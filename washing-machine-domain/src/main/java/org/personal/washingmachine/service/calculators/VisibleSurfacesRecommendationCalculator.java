package org.personal.washingmachine.service.calculators;

import com.google.common.annotations.VisibleForTesting;
import org.personal.washingmachine.entity.embedded.VisibleSurfaceDamage;
import org.personal.washingmachine.enums.Recommendation;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

import static org.personal.washingmachine.enums.Recommendation.*;

@Component
public class VisibleSurfacesRecommendationCalculator {

	private static final int VISIBLE_SURFACES_THRESHOLD = 5;

	public Recommendation calculate(VisibleSurfaceDamage detail) {
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
	Recommendation calculateForScratches(VisibleSurfaceDamage detail) {
		if (!detail.hasScratches()) {
			return NONE;
		}

		return (detail.getVisibleSurfacesScratchesLength() < VISIBLE_SURFACES_THRESHOLD)
				? RESALE
				: OUTLET;
	}

	@VisibleForTesting
	Recommendation calculateForDents(VisibleSurfaceDamage detail) {
		if (!detail.isVisibleSurfacesHasDents()) {
			return NONE;
		}

		return (detail.getVisibleSurfacesDentsDepth() < VISIBLE_SURFACES_THRESHOLD)
				? RESALE
				: OUTLET;
	}

	@VisibleForTesting
	Recommendation calculateForMinorDamage(VisibleSurfaceDamage detail) {
		return detail.isVisibleSurfacesHasMinorDamage()
				? RESALE
				: NONE;
	}

	@VisibleForTesting
	Recommendation calculateForMajorDamage(VisibleSurfaceDamage detail) {
		return (detail.isVisibleSurfacesHasMajorDamage())
				? OUTLET
				: NONE;
	}
}
