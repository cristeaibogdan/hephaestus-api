package org.personal.washingmachine.facade.dtos;

public record WashingMachineReportDTO (
        byte[] report,
        String createdAt
) {}
