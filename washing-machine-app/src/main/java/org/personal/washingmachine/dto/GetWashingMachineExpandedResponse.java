package org.personal.washingmachine.dto;

import java.util.List;

public record GetWashingMachineExpandedResponse(
        WashingMachineDetailDTO washingMachineDetail,
        List<WashingMachineImageDTO> washingMachineImages
) {}