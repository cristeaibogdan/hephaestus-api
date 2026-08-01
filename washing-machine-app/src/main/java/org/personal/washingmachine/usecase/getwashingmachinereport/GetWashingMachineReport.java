package org.personal.washingmachine.usecase.getwashingmachinereport;

import lombok.RequiredArgsConstructor;
import org.personal.washingmachine.dto.GetWashingMachineReportResponse;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.service.WashingMachineService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
class GetWashingMachineReport {
	private final WashingMachineService service;
	private final ReportGenerator reportGenerator;

	@GetMapping(value = "/v1/washing-machines/{serialNumber}/report")
	GetWashingMachineReportResponse handle(@PathVariable String serialNumber) {
		WashingMachine washingMachine = service.findBySerialNumber(serialNumber);
		return reportGenerator.getReport(washingMachine);
	}
}
