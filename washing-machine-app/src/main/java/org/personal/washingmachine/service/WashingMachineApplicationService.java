package org.personal.washingmachine.service;

import lombok.RequiredArgsConstructor;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.personal.washingmachine.dto.GetWashingMachineReportResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/washing-machines")
@RequiredArgsConstructor
class WashingMachineApplicationService {
	private final WashingMachineService service;
	private final WashingMachineRepository repository;
	private final WashingMachineReportGenerator reportGenerator;

	@GetMapping("/{serialNumber}/recommendation")
	public Recommendation getRecommendation(@PathVariable String serialNumber) {
		return repository.getRecommendation(serialNumber)
				.orElseThrow(() -> new CustomException(ErrorCode.SERIAL_NUMBER_NOT_FOUND, serialNumber));
	}

	@GetMapping(value = "/{serialNumber}/report")
	public GetWashingMachineReportResponse getReport(@PathVariable String serialNumber) {
		WashingMachine washingMachine = service.findBySerialNumber(serialNumber);
		return reportGenerator.getReport(washingMachine);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{serialNumber}")
	public void delete(@PathVariable String serialNumber) {
		if (!repository.existsBySerialNumber(serialNumber)) {
			throw new CustomException(ErrorCode.SERIAL_NUMBER_NOT_FOUND, serialNumber);
		}
		repository.deleteBySerialNumber(serialNumber);
	}
}
