package org.personal.washingmachine.dto;

public record GetWashingMachineReportResponse(
        byte[] report,
        String createdAt
) {}