package org.personal.washingmachine.service.calculators;

import org.personal.washingmachine.dto.WashingMachineDetailsDTO;
import org.personal.washingmachine.enums.Recommendation;

interface ICalculator {

	Recommendation calculate(WashingMachineDetailsDTO washingMachineDetailsDTO);
}
