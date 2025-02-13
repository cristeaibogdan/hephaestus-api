package org.personal.washingmachine.service;

import lombok.RequiredArgsConstructor;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.entity.WashingMachineDetail;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.service.calculators.HiddenSurfacesRecommendationCalculator;
import org.personal.washingmachine.service.calculators.PackageRecommendationCalculator;
import org.personal.washingmachine.service.calculators.PricingRecommendationCalculator;
import org.personal.washingmachine.service.calculators.VisibleSurfacesRecommendationCalculator;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

import static org.personal.washingmachine.enums.Recommendation.NONE;

@Service
@RequiredArgsConstructor
public class WashingMachineDamageCalculator {
	// TODO: Move inside embeded entities
	private final PackageRecommendationCalculator packageRecommendationCalculator;
	private final VisibleSurfacesRecommendationCalculator visibleSurfacesRecommendationCalculator;
	private final HiddenSurfacesRecommendationCalculator hiddenSurfacesRecommendationCalculator;
	private final PricingRecommendationCalculator pricingRecommendationCalculator;

	public Recommendation getRecommendation(WashingMachineDetail detail) {
		Recommendation recommendationForPackage = packageRecommendationCalculator.calculate(detail.getPackageDamage());
		Recommendation recommendationForVisibleSurfaces = visibleSurfacesRecommendationCalculator.calculate(detail.getVisibleSurfaceDamage());
		Recommendation recommendationForHiddenSurfaces = hiddenSurfacesRecommendationCalculator.calculate(detail.getHiddenSurfaceDamage());
		Recommendation recommendationForPricing = pricingRecommendationCalculator.calculate(detail.getPrice(), detail.getRepairPrice());

		Recommendation result = Collections.max(Arrays.asList(
				recommendationForPackage,
				recommendationForVisibleSurfaces,
				recommendationForHiddenSurfaces,
				recommendationForPricing
		));

		if (result == NONE) {
			throw new CustomException("Invalid recommendation issued " + NONE, ErrorCode.GENERAL);
		}

		return result;
	}
}
