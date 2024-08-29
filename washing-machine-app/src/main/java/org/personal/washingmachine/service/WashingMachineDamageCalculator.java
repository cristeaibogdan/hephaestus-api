package org.personal.washingmachine.service;

import lombok.RequiredArgsConstructor;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.dto.WashingMachineDetailsDTO;
import org.personal.washingmachine.dto.WashingMachineEvaluationDTO;
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

	public WashingMachineEvaluationDTO getDamageEvaluation(WashingMachineDetailsDTO dto) {
		int damageLevel = getDamageLevel(dto);
		String recommendation = getRecommendation(damageLevel);
		return new WashingMachineEvaluationDTO(damageLevel, recommendation);
	}

	int getDamageLevel(WashingMachineDetailsDTO dto) {
		DamageLevel damageLevelForPackage = packageDamageCalculator.calculate(dto);
		DamageLevel damageLevelForVisibleSurfaces = visibleSurfacesDamageCalculator.calculate(dto);
		DamageLevel damageLevelForHiddenSurfaces = hiddenSurfacesDamageCalculator.calculate(dto);
		DamageLevel damageLevelForPricing = pricingDamageCalculator.calculate(dto);

		return Collections.max(Arrays.asList(
				damageLevelForPackage.ordinal(),
				damageLevelForVisibleSurfaces.ordinal(),
				damageLevelForHiddenSurfaces.ordinal(),
				damageLevelForPricing.ordinal()
		));
	}

	//TODO: Use enum for 1-5, and recommendations. Get rid of the method.
	String getRecommendation(int damageLevel) {
		return switch (damageLevel) {
			case 1 -> "REPACKAGE";
			case 2 -> "RESALE";
			case 3 -> "OUTLET";
			case 4 -> "REPAIR";
			case 5 -> "DISASSEMBLE";
			default -> throw new CustomException("Invalid damage level: " + damageLevel, ErrorCode.GENERAL);
		};
	}
}
