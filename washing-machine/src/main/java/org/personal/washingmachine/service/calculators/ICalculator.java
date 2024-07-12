package org.personal.washingmachine.service.calculators;

import org.personal.washingmachine.entity.dtos.WashingMachineDetailsDTO;

interface ICalculator {

	int calculate(WashingMachineDetailsDTO washingMachineDetailsDTO);
}
