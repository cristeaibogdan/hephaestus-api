package org.personal.washingmachine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.personal.shared.clients.ProductClient;
import org.personal.washingmachine.TestData;
import org.personal.washingmachine.dto.*;
import org.personal.washingmachine.mapper.WashingMachineImageMapper;
import org.personal.washingmachine.mapper.WashingMachineMapper;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WashingMachineApplicationService.class)
@Import({WashingMachineImageMapper.class, WashingMachineMapper.class})
class CreateValidationMvcTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@MockBean WashingMachineService service;
	@MockBean WashingMachineRepository repository;
	@MockBean WashingMachineReportGenerator reportGenerator;
	@MockBean ProductClient productClient; //TODO: To be deleted

	static Stream<Arguments> getInvalidCreateWashingMachineRequestsWithNullValues() {
		return Stream.of(
				arguments(TestData.createCreateWashingMachineRequest().withCategory(null), "category"),
				arguments(TestData.createCreateWashingMachineRequest().withIdentificationMode(null), "identificationMode"),
				arguments(TestData.createCreateWashingMachineRequest().withManufacturer(null), "manufacturer"),
				arguments(TestData.createCreateWashingMachineRequest().withModel(null), "model"),
				arguments(TestData.createCreateWashingMachineRequest().withType(null), "type"),
				arguments(TestData.createCreateWashingMachineRequest().withSerialNumber(null), "serialNumber"),
				arguments(TestData.createCreateWashingMachineRequest().withReturnType(null), "returnType"),
				arguments(TestData.createCreateWashingMachineRequest().withDamageType(null), "damageType"),
				arguments(TestData.createCreateWashingMachineRequest().withWashingMachineDetail(null), "washingMachineDetail")
		);
	}

	@ParameterizedTest(name = "Validation fails for property {1}, with value null")
	@MethodSource("getInvalidCreateWashingMachineRequestsWithNullValues")
	void should_ThrowValidationException_When_CreateWashingMachineRequestPropertyIsNull(CreateWashingMachineRequest request, String propertyName) throws Exception {
		// GIVEN
		// WHEN
		ResultActions resultActions = performRequest(request);

		// THEN
		resultActions
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString(propertyName)));
	}

	static Stream<Arguments> getInvalidCreateWashingMachineRequestsWithBlankValues() {
		return Stream.of(
				arguments(TestData.createCreateWashingMachineRequest().withCategory("  "), "category"),
				arguments(TestData.createCreateWashingMachineRequest().withManufacturer("  "), "manufacturer"),
				arguments(TestData.createCreateWashingMachineRequest().withSerialNumber("  "), "serialNumber")
		);
	}

	@ParameterizedTest(name = "Validation fails for property {1}, with value (blank string)")
	@MethodSource("getInvalidCreateWashingMachineRequestsWithBlankValues")
	void should_ThrowValidationException_When_CreateWashingMachineRequestPropertyIsBlank(CreateWashingMachineRequest request, String propertyName) throws Exception {
		// GIVEN
		// WHEN
		ResultActions resultActions = performRequest(request);

		// THEN
		resultActions
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString(propertyName)));
	}


	static Stream<Arguments> getInvalidWashingMachineDetails() {
		return Stream.of(
				arguments(TestData.createValidWashingMachineDetailRequest().withVisibleSurfacesScratchesLength(-1), "visibleSurfacesScratchesLength", -1),
				arguments(TestData.createValidWashingMachineDetailRequest().withVisibleSurfacesScratchesLength(11), "visibleSurfacesScratchesLength", 11),
				arguments(TestData.createValidWashingMachineDetailRequest().withVisibleSurfacesDentsDepth(-1), "visibleSurfacesDentsDepth", -1),
				arguments(TestData.createValidWashingMachineDetailRequest().withVisibleSurfacesDentsDepth(11), "visibleSurfacesDentsDepth", 11),
				arguments(TestData.createValidWashingMachineDetailRequest().withVisibleSurfacesMinorDamage(null), "visibleSurfacesMinorDamage", null),
				arguments(TestData.createValidWashingMachineDetailRequest().withVisibleSurfacesMinorDamage("A".repeat(201)), "visibleSurfacesMinorDamage", "201 chars"),
				arguments(TestData.createValidWashingMachineDetailRequest().withVisibleSurfacesMajorDamage(null), "visibleSurfacesMajorDamage", null),
				arguments(TestData.createValidWashingMachineDetailRequest().withVisibleSurfacesMajorDamage("A".repeat(201)), "visibleSurfacesMajorDamage", "201 chars"),
				arguments(TestData.createValidWashingMachineDetailRequest().withHiddenSurfacesScratchesLength(-1), "hiddenSurfacesScratchesLength", -1),
				arguments(TestData.createValidWashingMachineDetailRequest().withHiddenSurfacesScratchesLength(11), "hiddenSurfacesScratchesLength", 11),
				arguments(TestData.createValidWashingMachineDetailRequest().withHiddenSurfacesDentsDepth(-1), "hiddenSurfacesDentsDepth", -1),
				arguments(TestData.createValidWashingMachineDetailRequest().withHiddenSurfacesDentsDepth(11), "hiddenSurfacesDentsDepth", 11),
				arguments(TestData.createValidWashingMachineDetailRequest().withHiddenSurfacesMinorDamage(null), "hiddenSurfacesMinorDamage", null),
				arguments(TestData.createValidWashingMachineDetailRequest().withHiddenSurfacesMinorDamage("A".repeat(201)), "hiddenSurfacesMinorDamage", "201 chars"),
				arguments(TestData.createValidWashingMachineDetailRequest().withHiddenSurfacesMajorDamage(null), "hiddenSurfacesMajorDamage", null),
				arguments(TestData.createValidWashingMachineDetailRequest().withHiddenSurfacesMajorDamage("A".repeat(201)), "hiddenSurfacesMajorDamage", "201 chars"),
				arguments(TestData.createValidWashingMachineDetailRequest().withPrice(-1), "price", -1),
				arguments(TestData.createValidWashingMachineDetailRequest().withRepairPrice(-1), "repairPrice", -1)
		);
	}

	@ParameterizedTest(name = "Validation fails for property {1}, with value {2}")
	@MethodSource("getInvalidWashingMachineDetails")
	void should_ThrowValidationException_When_ProvidedInvalidCreateWashingMachineDetailRequest(CreateWashingMachineRequest.WashingMachineDetailRequest dto, String propertyName, Object invalidValue) throws Exception {
		// GIVEN
		CreateWashingMachineRequest request = TestData.createCreateWashingMachineRequest().withWashingMachineDetail(dto);

		// WHEN
		ResultActions resultActions = performRequest(request);

		// THEN
		resultActions
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString(propertyName)));
	}

	static Stream<Arguments> getValidCreateWashingMachineDetailRequests() {
		return Stream.of(
				arguments(TestData.createValidWashingMachineDetailRequest().withVisibleSurfacesScratchesLength(0), "visibleSurfacesScratchesLength", 0),
				arguments(TestData.createValidWashingMachineDetailRequest().withVisibleSurfacesScratchesLength(10), "visibleSurfacesScratchesLength", 10),
				arguments(TestData.createValidWashingMachineDetailRequest().withVisibleSurfacesDentsDepth(0), "visibleSurfacesDentsDepth", 0),
				arguments(TestData.createValidWashingMachineDetailRequest().withVisibleSurfacesDentsDepth(10), "visibleSurfacesDentsDepth", 10),
				arguments(TestData.createValidWashingMachineDetailRequest().withVisibleSurfacesMinorDamage("B".repeat(200)), "visibleSurfacesMinorDamage", "200 chars"),
				arguments(TestData.createValidWashingMachineDetailRequest().withVisibleSurfacesMajorDamage("B".repeat(200)), "visibleSurfacesMajorDamage", "200 chars"),
				arguments(TestData.createValidWashingMachineDetailRequest().withHiddenSurfacesScratchesLength(0), "hiddenSurfacesScratchesLength", 0),
				arguments(TestData.createValidWashingMachineDetailRequest().withHiddenSurfacesScratchesLength(10), "hiddenSurfacesScratchesLength", 10),
				arguments(TestData.createValidWashingMachineDetailRequest().withHiddenSurfacesDentsDepth(0), "hiddenSurfacesDentsDepth", 0),
				arguments(TestData.createValidWashingMachineDetailRequest().withHiddenSurfacesDentsDepth(10), "hiddenSurfacesDentsDepth", 10),
				arguments(TestData.createValidWashingMachineDetailRequest().withHiddenSurfacesMinorDamage("B".repeat(200)), "hiddenSurfacesMinorDamage", "200 chars"),
				arguments(TestData.createValidWashingMachineDetailRequest().withHiddenSurfacesMajorDamage("B".repeat(200)), "hiddenSurfacesMajorDamage", "200 chars"),
				arguments(TestData.createValidWashingMachineDetailRequest().withPrice(0), "price", 0),
				arguments(TestData.createValidWashingMachineDetailRequest().withRepairPrice(0), "repairPrice", 0)
		);
	}

	@ParameterizedTest(name = "Validation passes for property {1}, with value {2}")
	@MethodSource("getValidCreateWashingMachineDetailRequests")
	void should_PassValidation_When_ProvidedValidCreateWashingMachineDetailRequest(CreateWashingMachineRequest.WashingMachineDetailRequest dto, String propertyName, Object validValue) throws Exception {
		// GIVEN
		CreateWashingMachineRequest request = TestData.createCreateWashingMachineRequest().withWashingMachineDetail(dto);

		// WHEN
		ResultActions resultActions = performRequest(request);

		// THEN
		resultActions.andExpect(status().isCreated());
	}

	private ResultActions performRequest(CreateWashingMachineRequest request) throws Exception {
		String jsonRequest = jackson.writeValueAsString(request);
		return mockMvc.perform(
				multipart("/api/v1/washing-machines/create")
						.file(new MockMultipartFile( // avoids error Content-Type 'application/octet-stream' is not supported
								"createWashingMachineRequest",
								"I_Don't_Matter",
								MediaType.APPLICATION_JSON_VALUE,
								jsonRequest.getBytes())
						)
						.file(new MockMultipartFile( // API expects at least 1 image when saving.
								"imageFiles",
								"whatever.jpeg",
								MediaType.IMAGE_JPEG_VALUE,
								"image content".getBytes())
						)
						.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
		);
	}
}