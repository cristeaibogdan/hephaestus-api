package org.personal.washingmachine.service.calculators;

import org.personal.washingmachine.dto.WashingMachineDetailsDTO;
import org.springframework.stereotype.Component;

import static org.personal.washingmachine.service.calculators.DamageLevel.*;

@Component
public class PricingDamageCalculator implements ICalculator {

	@Override
	public DamageLevel calculate(WashingMachineDetailsDTO dto) {
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
