package org.personal.washingmachine.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.personal.shared.clients.ProductClient;
import org.personal.washingmachine.facade.WashingMachineDamageCalculator;
import org.personal.washingmachine.facade.WashingMachineReportGenerator;
import org.personal.washingmachine.service.WashingMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WashingMachineController.class)
class WashingMachineControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WashingMachineService washingMachineServiceMock;

    @MockBean
    WashingMachineDamageCalculator washingMachineDamageCalculatorMock;

    @MockBean
    WashingMachineReportGenerator washingMachineReportGeneratorMock;

    @MockBean //TODO: To be deleted
    private ProductClient productClientMock;

    @Nested
    class testIsSerialNumberInUse {

        @Test
        void should_ReturnTrue() throws Exception {
            // GIVEN
            String serialNumber = "ABC-987";
            boolean expected = true;

            given(washingMachineServiceMock.isSerialNumberInUse(serialNumber))
                    .willReturn(true);

            // WHEN
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/washing-machines/{serialNumber}/validate",
                            serialNumber));

            // THEN
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(content().string(String.valueOf(expected)));
        }
    }
}