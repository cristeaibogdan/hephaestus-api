package org.personal.washingmachine.service.calculators;

import org.personal.washingmachine.dto.WashingMachineDetailDTO;
import org.personal.washingmachine.enums.Recommendation;

interface ICalculator {

	Recommendation calculate(WashingMachineDetailDTO washingMachineDetailDTO);
}
