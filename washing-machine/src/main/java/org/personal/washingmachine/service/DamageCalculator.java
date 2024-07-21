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

	// TODO ask:
	// 1. Should I break the dto and only send the properties I to each class calculator?
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
				damageLevelForPricing
		);
	}

	String getRecommendation(int damageLevel) {
		return switch (damageLevel) {
			case 1 -> "REPACKAGE";
			case 2, 3 -> "RESALE";
			case 4 -> "REPAIR";
			case 5 -> "DISASSEMBLE";
			default -> throw new CustomException("Invalid damage level: " + damageLevel, ErrorCode.GENERAL);
		};
	}
}
