package org.personal.washingmachine.dto;

import java.util.List;

public record GetWashingMachineExpandedResponse(
        GetWashingMachineDetailResponse washingMachineDetail,
        List<GetWashingMachineImageResponse> washingMachineImages
) {}