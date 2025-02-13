package org.personal.washingmachine.service.calculators;

import org.personal.washingmachine.entity.embedded.PackageDamage;
import org.personal.washingmachine.enums.Recommendation;
import org.springframework.stereotype.Component;

import static org.personal.washingmachine.enums.Recommendation.*;

@Component @Deprecated
public class PackageRecommendationCalculator {

	public Recommendation calculate(PackageDamage packageDamage) {
		if (packageDamage.isNotApplicable()) {
			return NONE;
		}

		return packageDamage.isPackageMaterialAvailable()
				? REPACKAGE
				: RESALE;
	}
}
