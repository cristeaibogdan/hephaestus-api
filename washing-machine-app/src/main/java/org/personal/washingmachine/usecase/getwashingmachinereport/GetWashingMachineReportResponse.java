package org.personal.washingmachine.usecase.getwashingmachinereport;

record GetWashingMachineReportResponse(
        byte[] report,
        String createdAt
) {}