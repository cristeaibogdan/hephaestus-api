package org.personal.washingmachine.usecase.checkserialnumberexists;

import lombok.RequiredArgsConstructor;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
class CheckSerialNumberExists {
	private final WashingMachineRepository repository;

	@GetMapping("/v1/washing-machines/{serialNumber}/validate")
	boolean handle(@PathVariable String serialNumber) {
		return repository.existsBySerialNumber(serialNumber);
	}
}
