package org.personal.washingmachine.facade.calculators;

import org.personal.washingmachine.dto.WashingMachineDetailsDTO;
import org.springframework.stereotype.Component;

@Component
public class PackageDamageCalculator implements ICalculator {

	@Override
	public int calculate(WashingMachineDetailsDTO dto) {
		if (!dto.applicablePackageDamage()) {
			return 0;
		}

		return dto.packageMaterialAvailable()
				? 1
				: 2;
	}
}