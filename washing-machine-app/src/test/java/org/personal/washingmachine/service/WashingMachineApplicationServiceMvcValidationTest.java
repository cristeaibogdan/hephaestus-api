package org.personal.washingmachine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.personal.shared.clients.ProductClient;
import org.personal.washingmachine.TestData;
import org.personal.washingmachine.dto.CreateWashingMachineDetailRequest;
import org.personal.washingmachine.dto.CreateWashingMachineRequest;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WashingMachineApplicationService.class)
class WashingMachineApplicationServiceMvcValidationTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@MockBean WashingMachineService service;
	@MockBean WashingMachineRepository repository;
	@MockBean WashingMachineDamageCalculator damageCalculator;
	@MockBean WashingMachineReportGenerator reportGenerator;
	@MockBean ProductClient productClient; //TODO: To be deleted

	static Stream<Arguments> getInvalidCreateWashingMachineRequestTestCases() {
		return Stream.of(
				arguments(TestData.createWashingMachineRequest().toBuilder().category(null).build(), "category", null),
				arguments(TestData.createWashingMachineRequest().toBuilder().identificationMode(null).build(), "identificationMode", null),
				arguments(TestData.createWashingMachineRequest().toBuilder().manufacturer(null).build(), "manufacturer", null),
				arguments(TestData.createWashingMachineRequest().toBuilder().model(null).build(), "model", null),
				arguments(TestData.createWashingMachineRequest().toBuilder().type(null).build(), "type", null),
				arguments(TestData.createWashingMachineRequest().toBuilder().serialNumber(null).build(), "serialNumber", null),
				arguments(TestData.createWashingMachineRequest().toBuilder().returnType(null).build(), "returnType", null),
				arguments(TestData.createWashingMachineRequest().toBuilder().damageType(null).build(), "damageType", null),
				arguments(TestData.createWashingMachineRequest().toBuilder().createWashingMachineDetailRequest(null).build(), "createWashingMachineDetailRequest", null)
		);
	}

	@ParameterizedTest(name = "Validation fails for property {1}, with value {2}")
	@MethodSource("getInvalidCreateWashingMachineRequestTestCases")
	void should_ThrowValidationException_When_ProvidedInvalidCreateWashingMachineRequest(CreateWashingMachineRequest request, String propertyName, Object invalidValue) throws Exception {
		// GIVEN
		String jsonRequest = jackson.writeValueAsString(request);

		MockMultipartFile jsonFile = new MockMultipartFile( // avoids error Content-Type 'application/octet-stream' is not supported
				"createWashingMachineRequest",
				"I_Don't_Matter",
				MediaType.APPLICATION_JSON_VALUE,
				jsonRequest.getBytes());

		// WHEN
		ResultActions resultActions = mockMvc.perform(
				multipart("/api/v1/washing-machines/save")
						.file(jsonFile)
						.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
		);

		// THEN
		resultActions
				.andExpect(status().is4xxClientError())
				.andExpect(content().string(containsString(propertyName)));
	}

	static Stream<Arguments> getInvalidCreateWashingMachineDetailRequestTestCases() {
		return Stream.of(
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().visibleSurfacesScratchesLength(-1).build(), "visibleSurfacesScratchesLength", -1),
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().visibleSurfacesScratchesLength(11).build(), "visibleSurfacesScratchesLength", 11),
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().visibleSurfacesDentsDepth(-1).build(), "visibleSurfacesDentsDepth", -1),
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().visibleSurfacesDentsDepth(11).build(), "visibleSurfacesDentsDepth", 11),
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().visibleSurfacesMinorDamage(null).build(), "visibleSurfacesMinorDamage", null),
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().visibleSurfacesMinorDamage("A".repeat(201)).build(), "visibleSurfacesMinorDamage", "201 chars"),
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().visibleSurfacesMajorDamage(null).build(), "visibleSurfacesMajorDamage", null),
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().visibleSurfacesMajorDamage("A".repeat(201)).build(), "visibleSurfacesMajorDamage", "201 chars"),
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().hiddenSurfacesScratchesLength(-1).build(), "hiddenSurfacesScratchesLength", -1),
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().hiddenSurfacesScratchesLength(11).build(), "hiddenSurfacesScratchesLength", 11),
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().hiddenSurfacesDentsDepth(-1).build(), "hiddenSurfacesDentsDepth", -1),
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().hiddenSurfacesDentsDepth(11).build(), "hiddenSurfacesDentsDepth", 11),
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().hiddenSurfacesMinorDamage(null).build(), "hiddenSurfacesMinorDamage", null),
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().hiddenSurfacesMinorDamage("A".repeat(201)).build(), "hiddenSurfacesMinorDamage", "201 chars"),
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().hiddenSurfacesMajorDamage(null).build(), "hiddenSurfacesMajorDamage", null),
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().hiddenSurfacesMajorDamage("A".repeat(201)).build(), "hiddenSurfacesMajorDamage", "201 chars"),
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().price(-1).build(), "price", -1),
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().repairPrice(-1).build(), "repairPrice", -1)
		);
	}

	@ParameterizedTest(name = "Validation fails for property {1}, with value {2}")
	@MethodSource("getInvalidCreateWashingMachineDetailRequestTestCases")
	void should_ThrowValidationException_When_ProvidedInvalidCreateWashingMachineDetailRequest(CreateWashingMachineDetailRequest dto, String propertyName, Object invalidValue) throws Exception {
		// GIVEN
		CreateWashingMachineRequest request = TestData.createWashingMachineRequest().toBuilder().createWashingMachineDetailRequest(dto).build();
		String jsonRequest = jackson.writeValueAsString(request);

		MockMultipartFile jsonFile = new MockMultipartFile( // avoids error Content-Type 'application/octet-stream' is not supported
				"createWashingMachineRequest",
				"I_Don't_Matter",
				MediaType.APPLICATION_JSON_VALUE,
				jsonRequest.getBytes());

		// WHEN
		ResultActions resultActions = mockMvc.perform(
				multipart("/api/v1/washing-machines/save")
						.file(jsonFile)
						.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
		);

		// THEN
		resultActions
				.andExpect(status().is4xxClientError())
				.andExpect(content().string(containsString(propertyName)));
	}

	static Stream<Arguments> getValidCreateWashingMachineDetailRequestTestCases() {
		return Stream.of(
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().visibleSurfacesScratchesLength(0).build(), "visibleSurfacesScratchesLength", 0),
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().visibleSurfacesScratchesLength(10).build(), "visibleSurfacesScratchesLength", 10),
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().visibleSurfacesDentsDepth(0).build(), "visibleSurfacesDentsDepth", 0),
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().visibleSurfacesDentsDepth(10).build(), "visibleSurfacesDentsDepth", 10),
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().visibleSurfacesMinorDamage("B".repeat(200)).build(), "visibleSurfacesMinorDamage", "200 chars"),
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().visibleSurfacesMajorDamage("B".repeat(200)).build(), "visibleSurfacesMajorDamage", "200 chars"),
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().hiddenSurfacesScratchesLength(0).build(), "hiddenSurfacesScratchesLength", 0),
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().hiddenSurfacesScratchesLength(10).build(), "hiddenSurfacesScratchesLength", 10),
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().hiddenSurfacesDentsDepth(0).build(), "hiddenSurfacesDentsDepth", 0),
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().hiddenSurfacesDentsDepth(10).build(), "hiddenSurfacesDentsDepth", 10),
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().hiddenSurfacesMinorDamage("B".repeat(200)).build(), "hiddenSurfacesMinorDamage", "200 chars"),
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().hiddenSurfacesMajorDamage("B".repeat(200)).build(), "hiddenSurfacesMajorDamage", "200 chars"),
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().price(0).build(), "price", 0),
				arguments(TestData.createWashingMachineDetailRequest().toBuilder().repairPrice(0).build(), "repairPrice", 0)
		);
	}

	@ParameterizedTest(name = "Validation passes for property {1}, with value {2}")
	@MethodSource("getValidCreateWashingMachineDetailRequestTestCases")
	void should_PassValidation_When_ProvidedValidCreateWashingMachineDetailRequest(CreateWashingMachineDetailRequest dto, String propertyName, Object validValue) throws Exception {
		// GIVEN
		CreateWashingMachineRequest request = TestData.createWashingMachineRequest().toBuilder().createWashingMachineDetailRequest(dto).build();
		String jsonRequest = jackson.writeValueAsString(request);

		MockMultipartFile jsonFile = new MockMultipartFile( // avoids error Content-Type 'application/octet-stream' is not supported
				"createWashingMachineRequest",
				"I_Don't_Matter",
				MediaType.APPLICATION_JSON_VALUE,
				jsonRequest.getBytes());

		MockMultipartFile mockFile = new MockMultipartFile(
				"imageFiles",
				"whatever.jpeg",
				MediaType.IMAGE_JPEG_VALUE,
				"image content".getBytes()
		);

		// WHEN
		ResultActions resultActions = mockMvc.perform(
				multipart("/api/v1/washing-machines/save")
						.file(jsonFile)
						.file(mockFile)
						.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
		);

		// THEN
		resultActions
				.andExpect(status().is2xxSuccessful())
				.andExpect(content().string(not(containsString(propertyName))));
	}
}