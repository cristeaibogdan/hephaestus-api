package org.personal.washingmachine.usecase.deletewashingmachine;

import lombok.RequiredArgsConstructor;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
class DeleteWashingMachine {
	private final WashingMachineRepository repository;

	static final String PATH = "/v1/washing-machines/{serialNumber}";

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(PATH)
	void handle(@PathVariable String serialNumber) {
		if (!repository.existsBySerialNumber(serialNumber)) {
			throw new CustomException(ErrorCode.SERIAL_NUMBER_NOT_FOUND, serialNumber);
		}
		repository.deleteBySerialNumber(serialNumber);
	}
}
