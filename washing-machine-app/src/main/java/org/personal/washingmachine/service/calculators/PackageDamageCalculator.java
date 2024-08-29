package org.personal.washingmachine.service.calculators;

import org.personal.washingmachine.dto.WashingMachineDetailsDTO;
import org.springframework.stereotype.Component;

import static org.personal.washingmachine.service.calculators.DamageLevel.*;

@Component
public class PackageDamageCalculator implements ICalculator {

	@Override
	public DamageLevel calculate(WashingMachineDetailsDTO dto) {
		if (!dto.applicablePackageDamage()) {
			return NONE;
		}

		return dto.packageMaterialAvailable()
				? REPACKAGE
				: RESALE;
	}
}
