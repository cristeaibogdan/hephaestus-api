package org.personal.washingmachine.service;

import lombok.RequiredArgsConstructor;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.dto.WashingMachineDetailDTO;
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

	private final PackageRecommendationCalculator packageRecommendationCalculator;
	private final VisibleSurfacesRecommendationCalculator visibleSurfacesRecommendationCalculator;
	private final HiddenSurfacesRecommendationCalculator hiddenSurfacesRecommendationCalculator;
	private final PricingRecommendationCalculator pricingRecommendationCalculator;

	public Recommendation getRecommendation(WashingMachineDetailDTO dto) {
		Recommendation recommendationForPackage = packageRecommendationCalculator.calculate(dto);
		Recommendation recommendationForVisibleSurfaces = visibleSurfacesRecommendationCalculator.calculate(dto);
		Recommendation recommendationForHiddenSurfaces = hiddenSurfacesRecommendationCalculator.calculate(dto);
		Recommendation recommendationForPricing = pricingRecommendationCalculator.calculate(dto);

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
