package org.personal.washingmachine.entity.dtos;

import java.util.List;

public record WashingMachineExpandedDTO(
        WashingMachineDetailsDTO washingMachineDetails,
        List<WashingMachineImageDTO> washingMachineImages
) {}
