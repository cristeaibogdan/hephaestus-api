package org.personal.washingmachine.service;

import lombok.RequiredArgsConstructor;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.dto.WashingMachineDetailsDTO;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.service.calculators.HiddenSurfacesDamageCalculator;
import org.personal.washingmachine.service.calculators.PackageDamageCalculator;
import org.personal.washingmachine.service.calculators.PricingDamageCalculator;
import org.personal.washingmachine.service.calculators.VisibleSurfacesDamageCalculator;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

import static org.personal.washingmachine.enums.Recommendation.NONE;

@Service
@RequiredArgsConstructor
public class WashingMachineDamageCalculator {

	private final PackageDamageCalculator packageDamageCalculator;
	private final VisibleSurfacesDamageCalculator visibleSurfacesDamageCalculator;
	private final HiddenSurfacesDamageCalculator hiddenSurfacesDamageCalculator;
	private final PricingDamageCalculator pricingDamageCalculator;

	public Recommendation getRecommendation(WashingMachineDetailsDTO dto) {
		Recommendation recommendationForPackage = packageDamageCalculator.calculate(dto);
		Recommendation recommendationForVisibleSurfaces = visibleSurfacesDamageCalculator.calculate(dto);
		Recommendation recommendationForHiddenSurfaces = hiddenSurfacesDamageCalculator.calculate(dto);
		Recommendation recommendationForPricing = pricingDamageCalculator.calculate(dto);

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
