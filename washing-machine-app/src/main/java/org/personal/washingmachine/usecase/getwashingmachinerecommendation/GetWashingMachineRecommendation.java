package org.personal.washingmachine.usecase.getwashingmachinerecommendation;

import lombok.RequiredArgsConstructor;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
class GetWashingMachineRecommendation {
	private final WashingMachineRepository repository;

	@GetMapping("/v1/washing-machines/{serialNumber}/recommendation")
	Recommendation handle(@PathVariable String serialNumber) {
		return repository.getRecommendation(serialNumber)
				.orElseThrow(() -> new CustomException(ErrorCode.SERIAL_NUMBER_NOT_FOUND, serialNumber));
	}
}
