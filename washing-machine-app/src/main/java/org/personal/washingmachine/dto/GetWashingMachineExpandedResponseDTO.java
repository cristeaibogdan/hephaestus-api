package org.personal.washingmachine.dto;

import java.util.List;

public record GetWashingMachineExpandedResponseDTO(
        WashingMachineDetailDTO washingMachineDetail,
        List<WashingMachineImageDTO> washingMachineImages
) {}
