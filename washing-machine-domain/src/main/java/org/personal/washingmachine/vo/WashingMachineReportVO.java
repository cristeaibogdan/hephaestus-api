package org.personal.washingmachine.vo;

public record WashingMachineReportVO(
        byte[] report,
        String createdAt
) {}
