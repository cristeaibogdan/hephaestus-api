package org.personal.washingmachine.service.calculators;

import org.personal.washingmachine.dto.WashingMachineDetailDTO;
import org.personal.washingmachine.enums.Recommendation;
import org.springframework.stereotype.Component;

import static org.personal.washingmachine.enums.Recommendation.*;

@Component
public class PricingRecommendationCalculator {

	public Recommendation calculate(Integer price, Integer repairPrice) {
		if (price == null || repairPrice == null) { // TODO: can be replaced with DTO validation
			return NONE;
		}

		if (price <= 0 || repairPrice <= 0) { // TODO: negative numbers can be validated in DTO
			return NONE;
		}

		boolean repairPriceExceedsHalfTheProductPrice = (repairPrice >= price * 0.5);

		return repairPriceExceedsHalfTheProductPrice
				? DISASSEMBLE
				: REPAIR;
	}
}
