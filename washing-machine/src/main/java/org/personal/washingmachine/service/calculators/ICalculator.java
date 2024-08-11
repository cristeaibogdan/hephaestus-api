package org.personal.washingmachine.service.calculators;

import org.personal.washingmachine.facade.dtos.WashingMachineDetailsDTO;

interface ICalculator {

	int calculate(WashingMachineDetailsDTO washingMachineDetailsDTO);
}
