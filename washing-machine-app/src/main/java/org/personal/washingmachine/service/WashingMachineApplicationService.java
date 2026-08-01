package org.personal.washingmachine.service;

import lombok.RequiredArgsConstructor;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/washing-machines")
@RequiredArgsConstructor
class WashingMachineApplicationService {
	private final WashingMachineRepository repository;

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{serialNumber}")
	public void delete(@PathVariable String serialNumber) {
		if (!repository.existsBySerialNumber(serialNumber)) {
			throw new CustomException(ErrorCode.SERIAL_NUMBER_NOT_FOUND, serialNumber);
		}
		repository.deleteBySerialNumber(serialNumber);
	}
}
