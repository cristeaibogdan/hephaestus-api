package org.personal.washingmachine.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.entity.dtos.WashingMachineDetailsDTO;
import org.personal.washingmachine.entity.dtos.WashingMachineEvaluationDTO;
import org.personal.washingmachine.service.calculators.HiddenSurfacesDamageCalculator;
import org.personal.washingmachine.service.calculators.PackageDamageCalculator;
import org.personal.washingmachine.service.calculators.PricingDamageCalculator;
import org.personal.washingmachine.service.calculators.VisibleSurfacesDamageCalculator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DamageCalculator {

	private final PackageDamageCalculator packageDamageCalculator;
	private final VisibleSurfacesDamageCalculator visibleSurfacesDamageCalculator;
	private final HiddenSurfacesDamageCalculator hiddenSurfacesDamageCalculator;
	private final PricingDamageCalculator pricingDamageCalculator;

//	TODO: Ask:
//	1. The DTO is flat with 10+ params, should i pass only what's needed to each method so i can test easily?
//	As of now I use @Builder on the DTO which allows me to create instances only with certain parameters
//	2. If I test the underlying methods, is there any reason to test the method that uses those methods?
//	3. How should I handle the constant I put in VisibleSurfacesDamageCalculator in my tests?
//	Should I make it public / package private?

	public WashingMachineEvaluationDTO generateWashingMachineDamageEvaluation(WashingMachineDetailsDTO dto) {
		int damageLevel = getDamageLevel(dto);
		String recommendation = getRecommendation(damageLevel);
		return new WashingMachineEvaluationDTO(damageLevel, recommendation);
	}

	int getDamageLevel(WashingMachineDetailsDTO dto) {

		int damageLevelForPackage = packageDamageCalculator.calculate(dto);
		int damageLevelForVisibleSurfaces = visibleSurfacesDamageCalculator.calculate(dto);
		int damageLevelForHiddenSurfaces = hiddenSurfacesDamageCalculator.calculate(dto);
		int damageLevelForPricing = pricingDamageCalculator.calculate(dto);

		return NumberUtils.max(
				damageLevelForPackage,
				damageLevelForVisibleSurfaces,
				damageLevelForHiddenSurfaces,
				damageLevelForPricing);
	}

	String getRecommendation(int damageLevel) {
		return switch (damageLevel) {
			case 1 -> "REPACKAGE";
			case 2, 3 -> "RESALE";
			case 4 -> "REPAIR";
			case 5 -> "DISASSEMBLE";
			default -> throw new CustomException(ErrorCode.E_1009, "Invalid damage level");
		};
	}
}
