package org.personal.washingmachine.service.calculators;

import org.personal.washingmachine.dto.WashingMachineDetailDTO;
import org.personal.washingmachine.enums.Recommendation;
import org.springframework.stereotype.Component;

import static org.personal.washingmachine.enums.Recommendation.*;

@Component
public class PricingRecommendationCalculator implements ICalculator {

	@Override
	public Recommendation calculate(WashingMachineDetailDTO dto) {
		if (dto.price() == null || dto.repairPrice() == null) {
			return NONE;
		}

		if (dto.price() <= 0 || dto.repairPrice() <= 0) {
			return NONE;
		}

		boolean repairPriceExceedsHalfTheProductPrice = (dto.repairPrice() >= dto.price() * 0.5);

		return repairPriceExceedsHalfTheProductPrice
				? DISASSEMBLE
				: REPAIR;
	}
}
