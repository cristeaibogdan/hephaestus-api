package org.personal.washingmachine.dto;

import java.util.List;

public record GetWashingMachineExpandedResponse(
        GetWashingMachineDetailResponse getWashingMachineDetailResponse,
        List<WashingMachineImageDTO> washingMachineImages
) {}