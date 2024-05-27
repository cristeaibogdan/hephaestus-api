package org.personal.washingmachine.entity.dtos;

public record WashingMachineReportDTO (
        byte[] report,
        String createdAt
) {}
