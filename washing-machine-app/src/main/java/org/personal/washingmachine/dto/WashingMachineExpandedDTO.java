package org.personal.washingmachine.dto;

import java.util.List;

public record WashingMachineExpandedDTO(
        WashingMachineDetailDTO washingMachineDetail,
        List<WashingMachineImageDTO> washingMachineImages
) {}
