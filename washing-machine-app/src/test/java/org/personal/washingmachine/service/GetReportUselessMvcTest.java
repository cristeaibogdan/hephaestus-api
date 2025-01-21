package org.personal.washingmachine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.personal.shared.clients.ProductClient;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Deprecated @Disabled
@WebMvcTest(WashingMachineApplicationService.class)
class GetReportUselessMvcTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@MockBean WashingMachineService service;
	@MockBean WashingMachineRepository repository;
	@MockBean WashingMachineDamageCalculator damageCalculator;
	@MockBean WashingMachineReportGenerator reportGenerator;
	@MockBean ProductClient productClient; //TODO: To be deleted

	/* // TODO: POINT OF DISCUSSION
	What is really tested in here?
	If I go to .findBySerialNumber and delete the custom thrown exception this test will remain green.
	Move this into an integration test. See GetReportIntegrationTest. Where you test the logic (that the exception is thrown by the code),
	the status code and the message sent back to the client.

	Example:
	A. If the exception is thrown into a mocked bean of the controller:
	WashingMachineApplication ==> WashingMachineService (exception thrown here)
	You have no other choice but to use given-willThrow to FAKE a custom exception.
	Meaning you can delete the production code that throws the exception and the test will stay green. Evergreen test.

	B. If the exception is thrown into the controller(WashingMachineApplication):
	WashingMachineApplication (exception thrown here) ==> WashingMachineService
	You don't need to use given-willThrow to fake an exception.
	And if you delete the production code, the test will fail and notify you.
	However, this makes refactoring painful. If you decide to move the exception deeper into the service,
	you'll have to move this test to an IntegrationTest. Where you can check logic, status and content.

	 In case A, an alternative would be to create a social test for WashingMachineService and mock everything.
	 Then write a test to check if the code throws an exception. All good, but you can't test the http status,
	 nor the message sent to the client. Therefore IntegrationTest is more appealing, you end up testing both logic, status and content.
	 */
	@Test
	void should_ThrowCustomException_When_SerialNumberNotFound() throws Exception {
		// GIVEN
		String serialNumber = "I don't exist in DB";

		given(service.findBySerialNumber(serialNumber))
				.willThrow(new CustomException(ErrorCode.SERIAL_NUMBER_NOT_FOUND, serialNumber));

		// WHEN
		ResultActions resultActions = performRequest(serialNumber);

		// THEN
		resultActions
				.andExpect(status().isNotFound())
				.andExpect(content().string(not(containsString("Internal Translation Error"))));
	}

	private ResultActions performRequest(String serialNumber) throws Exception {
		return mockMvc.perform(
				get("/api/v1/washing-machines/{serialNumber}/report", serialNumber));
	}
}
