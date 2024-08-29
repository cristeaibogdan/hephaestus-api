package org.personal.washingmachine.service.calculators;

import org.personal.washingmachine.dto.WashingMachineDetailsDTO;
import org.personal.washingmachine.enums.Recommendation;
import org.springframework.stereotype.Component;

import static org.personal.washingmachine.enums.Recommendation.*;

@Component
public class PackageRecommendationCalculator implements ICalculator {

	@Override
	public Recommendation calculate(WashingMachineDetailsDTO dto) {
		if (!dto.applicablePackageDamage()) {
			return NONE;
		}

		return dto.packageMaterialAvailable()
				? REPACKAGE
				: RESALE;
	}
}
