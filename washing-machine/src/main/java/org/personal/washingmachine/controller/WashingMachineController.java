package org.personal.washingmachine.controller;

import lombok.RequiredArgsConstructor;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.entity.dtos.*;
import org.personal.washingmachine.service.WashingMachineService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/washing-machines")
@RestController
class WashingMachineController {

    private final WashingMachineService washingMachineService;

    @PostMapping
    Page<WashingMachineDTO> getPaginatedAndFilteredWashingMachines(@RequestBody PageRequestDTO pageRequestDTO) {
        return washingMachineService.getPaginatedAndFilteredWashingMachines(pageRequestDTO);
    }

    @GetMapping("/{serialNumber}/expanded")
    WashingMachineExpandedDTO getWashingMachineExpanded(@PathVariable String serialNumber) {
        return washingMachineService.getWashingMachineExpanded(serialNumber);
    }

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    void saveWashingMachine(
            @RequestPart WashingMachine washingMachine,
            @RequestPart List<MultipartFile> imageFiles) {
        washingMachineService.saveWashingMachine(washingMachine, imageFiles);
    }

    @PostMapping("/evaluate")
    WashingMachineEvaluationDTO generateWashingMachineDamageEvaluation(
            @RequestBody WashingMachineDetailsDTO washingMachineDetailsDTO) {

        return washingMachineService.generateWashingMachineDamageEvaluation(washingMachineDetailsDTO);
    }

    @GetMapping(value = "/{serialNumber}/report")
    WashingMachineReportDTO getWashingMachineReport(@PathVariable String serialNumber) {
        return washingMachineService.getWashingMachineReport(serialNumber);
    }

//*********************************************************************************************
//******************** ENDPOINTS FOR ASYNC VALIDATORS
//*********************************************************************************************

    @GetMapping("/{serialNumber}/validate")
    boolean isSerialNumberInUse(@PathVariable String serialNumber) {
        return washingMachineService.isSerialNumberInUse(serialNumber);
    }

}
