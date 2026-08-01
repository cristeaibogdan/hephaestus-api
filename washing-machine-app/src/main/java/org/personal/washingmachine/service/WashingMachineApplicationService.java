package org.personal.washingmachine.service;

import lombok.RequiredArgsConstructor;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.dto.*;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.personal.washingmachine.dto.GetWashingMachineReportResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.personal.washingmachine.mapper.WashingMachineMapper;

@RestController
@RequestMapping("/v1/washing-machines")
@RequiredArgsConstructor
class WashingMachineApplicationService {
	private final WashingMachineService service;
	private final WashingMachineRepository repository;
	private final WashingMachineReportGenerator reportGenerator;

	private final WashingMachineMapper washingMachineMapper;

	/**
	 * @deprecated
	 * <p> <b>In the context of</b> providing an API for retrieving a single {@link org.personal.washingmachine.dto.GetWashingMachineFullResponse} by serial number, </p>
	 * <p> <b>facing</b> the concern that clients might call this method in a loop to retrieve multiple DTOs, </p>
	 * <p> <b>we decided</b> to deprecate this method and introduce {@link org.personal.washingmachine.service.WashingMachineApplicationService#loadMany} </p>
	 * <p> <b>to achieve</b> improved performance by reducing the number of network requests, </p>
	 * <p> <b>accepting</b> that clients need to wrap single serial numbers in a list. </p>
	 * <p> Use {@link org.personal.washingmachine.service.WashingMachineApplicationService#loadMany} instead.
	 */
	@Deprecated(since = "2024/11/21")
	@GetMapping("/{serialNumber}")
	public GetWashingMachineFullResponse load(@PathVariable String serialNumber) {
		WashingMachine washingMachine = service.findBySerialNumber(serialNumber);
		return washingMachineMapper.toGetWashingMachineFullResponse(washingMachine);
	}

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
