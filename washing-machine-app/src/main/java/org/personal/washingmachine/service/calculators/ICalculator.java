package org.personal.washingmachine.service.calculators;

import org.personal.washingmachine.dto.WashingMachineDetailsDTO;

interface ICalculator {

	DamageLevel calculate(WashingMachineDetailsDTO washingMachineDetailsDTO);
}
