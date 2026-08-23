package org.personal.washingmachine.usecase.existsbyserialnumber;

import lombok.RequiredArgsConstructor;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
class ExistsBySerialNumber {
	private final WashingMachineRepository repository;

	static final String PATH = "/v1/washing-machines/{serialNumber}/exists";

	@GetMapping(PATH)
	boolean handle(@PathVariable String serialNumber) {
		return repository.existsBySerialNumber(serialNumber);
	}
}
