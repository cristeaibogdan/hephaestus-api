package org.personal.washingmachine.service.calculators;

import org.personal.washingmachine.entity.dtos.WashingMachineDetailsDTO;
import org.springframework.stereotype.Component;

@Component
public class PricingDamageCalculator implements ICalculator {

	@Override
	public int calculate(WashingMachineDetailsDTO dto) {
		if (dto.price() == null || dto.repairPrice() == null) {
			return 0;
		}

		if (dto.price() <= 0 && dto.repairPrice() <= 0) {
			return 0;
		}

		boolean repairPriceExceedsHalfTheProductPrice = (dto.repairPrice() >= dto.price() * 0.5);

		return repairPriceExceedsHalfTheProductPrice
				? 5
				: 4;

	}
}
