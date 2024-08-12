package org.personal.washingmachine.dto;

import java.util.List;

public record WashingMachineExpandedDTO(
        WashingMachineDetailsDTO washingMachineDetails,
        List<WashingMachineImageDTO> washingMachineImages
) {}
