package org.personal.washingmachine.dto;

public record WashingMachineReportDTO (
        byte[] report,
        String createdAt
) {}
