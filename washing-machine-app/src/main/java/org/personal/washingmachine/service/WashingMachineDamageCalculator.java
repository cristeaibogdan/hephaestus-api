package org.personal.washingmachine.service;

import lombok.RequiredArgsConstructor;
import org.personal.washingmachine.dto.WashingMachineDetailsDTO;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.service.calculators.*;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

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

		return Collections.max(Arrays.asList(
				recommendationForPackage,
				recommendationForVisibleSurfaces,
				recommendationForHiddenSurfaces,
				recommendationForPricing
		));
	}
}
