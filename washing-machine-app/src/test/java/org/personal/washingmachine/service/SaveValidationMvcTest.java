package org.personal.washingmachine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.personal.shared.clients.ProductClient;
import org.personal.washingmachine.TestData;
import org.personal.washingmachine.dto.*;
import org.personal.washingmachine.mapper.WashingMachineDetailMapper;
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
@Import({WashingMachineImageMapper.class, WashingMachineDetailMapper.class, WashingMachineMapper.class})
class SaveValidationMvcTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@MockBean WashingMachineService service;
	@MockBean WashingMachineRepository repository;
	@MockBean WashingMachineReportGenerator reportGenerator;
	@MockBean ProductClient productClient; //TODO: To be deleted

	static Stream<Arguments> getInvalidCreateWashingMachineRequests() {
		return Stream.of(
				arguments(TestData.createSaveWashingMachineRequest().toBuilder().category(null).build(), "category", null),
				arguments(TestData.createSaveWashingMachineRequest().toBuilder().category("  ").build(), "category", " (blank string) "),
				arguments(TestData.createSaveWashingMachineRequest().toBuilder().identificationMode(null).build(), "identificationMode", null),
				arguments(TestData.createSaveWashingMachineRequest().toBuilder().manufacturer(null).build(), "manufacturer", null),
				arguments(TestData.createSaveWashingMachineRequest().toBuilder().manufacturer("  ").build(), "manufacturer", " (blank string) "),
				arguments(TestData.createSaveWashingMachineRequest().toBuilder().model(null).build(), "model", null),
				arguments(TestData.createSaveWashingMachineRequest().toBuilder().type(null).build(), "type", null),
				arguments(TestData.createSaveWashingMachineRequest().toBuilder().serialNumber(null).build(), "serialNumber", null),
				arguments(TestData.createSaveWashingMachineRequest().toBuilder().serialNumber("  ").build(), "serialNumber", " (blank string) "),
				arguments(TestData.createSaveWashingMachineRequest().toBuilder().returnType(null).build(), "returnType", null),
				arguments(TestData.createSaveWashingMachineRequest().toBuilder().damageType(null).build(), "damageType", null),
				arguments(TestData.createSaveWashingMachineRequest().toBuilder().saveWashingMachineDetailRequest(null).build(), "saveWashingMachineDetailRequest", null)
		);
	}

	@ParameterizedTest(name = "Validation fails for property {1}, with value {2}")
	@MethodSource("getInvalidCreateWashingMachineRequests")
	void should_ThrowValidationException_When_ProvidedInvalidCreateWashingMachineRequest(SaveWashingMachineRequest request, String propertyName, Object invalidValue) throws Exception {
		// GIVEN
		// WHEN
		ResultActions resultActions = performRequest(request);

		// THEN
		resultActions
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString(propertyName)));
	}

	static Stream<Arguments> getInvalidCreateWashingMachineDetailRequests() {
		return Stream.of(
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().visibleSurfacesScratchesLength(-1).build(), "visibleSurfacesScratchesLength", -1),
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().visibleSurfacesScratchesLength(11).build(), "visibleSurfacesScratchesLength", 11),
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().visibleSurfacesDentsDepth(-1).build(), "visibleSurfacesDentsDepth", -1),
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().visibleSurfacesDentsDepth(11).build(), "visibleSurfacesDentsDepth", 11),
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().visibleSurfacesMinorDamage(null).build(), "visibleSurfacesMinorDamage", null),
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().visibleSurfacesMinorDamage("A".repeat(201)).build(), "visibleSurfacesMinorDamage", "201 chars"),
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().visibleSurfacesMajorDamage(null).build(), "visibleSurfacesMajorDamage", null),
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().visibleSurfacesMajorDamage("A".repeat(201)).build(), "visibleSurfacesMajorDamage", "201 chars"),
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().hiddenSurfacesScratchesLength(-1).build(), "hiddenSurfacesScratchesLength", -1),
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().hiddenSurfacesScratchesLength(11).build(), "hiddenSurfacesScratchesLength", 11),
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().hiddenSurfacesDentsDepth(-1).build(), "hiddenSurfacesDentsDepth", -1),
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().hiddenSurfacesDentsDepth(11).build(), "hiddenSurfacesDentsDepth", 11),
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().hiddenSurfacesMinorDamage(null).build(), "hiddenSurfacesMinorDamage", null),
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().hiddenSurfacesMinorDamage("A".repeat(201)).build(), "hiddenSurfacesMinorDamage", "201 chars"),
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().hiddenSurfacesMajorDamage(null).build(), "hiddenSurfacesMajorDamage", null),
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().hiddenSurfacesMajorDamage("A".repeat(201)).build(), "hiddenSurfacesMajorDamage", "201 chars"),
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().price(-1).build(), "price", -1),
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().repairPrice(-1).build(), "repairPrice", -1)
		);
	}

	@ParameterizedTest(name = "Validation fails for property {1}, with value {2}")
	@MethodSource("getInvalidCreateWashingMachineDetailRequests")
	void should_ThrowValidationException_When_ProvidedInvalidCreateWashingMachineDetailRequest(SaveWashingMachineDetailRequest dto, String propertyName, Object invalidValue) throws Exception {
		// GIVEN
		SaveWashingMachineRequest request = TestData.createSaveWashingMachineRequest().toBuilder().saveWashingMachineDetailRequest(dto).build();

		// WHEN
		ResultActions resultActions = performRequest(request);

		// THEN
		resultActions
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString(propertyName)));
	}

	static Stream<Arguments> getValidCreateWashingMachineDetailRequests() {
		return Stream.of(
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().visibleSurfacesScratchesLength(0).build(), "visibleSurfacesScratchesLength", 0),
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().visibleSurfacesScratchesLength(10).build(), "visibleSurfacesScratchesLength", 10),
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().visibleSurfacesDentsDepth(0).build(), "visibleSurfacesDentsDepth", 0),
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().visibleSurfacesDentsDepth(10).build(), "visibleSurfacesDentsDepth", 10),
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().visibleSurfacesMinorDamage("B".repeat(200)).build(), "visibleSurfacesMinorDamage", "200 chars"),
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().visibleSurfacesMajorDamage("B".repeat(200)).build(), "visibleSurfacesMajorDamage", "200 chars"),
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().hiddenSurfacesScratchesLength(0).build(), "hiddenSurfacesScratchesLength", 0),
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().hiddenSurfacesScratchesLength(10).build(), "hiddenSurfacesScratchesLength", 10),
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().hiddenSurfacesDentsDepth(0).build(), "hiddenSurfacesDentsDepth", 0),
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().hiddenSurfacesDentsDepth(10).build(), "hiddenSurfacesDentsDepth", 10),
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().hiddenSurfacesMinorDamage("B".repeat(200)).build(), "hiddenSurfacesMinorDamage", "200 chars"),
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().hiddenSurfacesMajorDamage("B".repeat(200)).build(), "hiddenSurfacesMajorDamage", "200 chars"),
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().price(0).build(), "price", 0),
				arguments(TestData.createValidSaveWashingMachineDetailRequest().toBuilder().repairPrice(0).build(), "repairPrice", 0)
		);
	}

	@ParameterizedTest(name = "Validation passes for property {1}, with value {2}")
	@MethodSource("getValidCreateWashingMachineDetailRequests")
	void should_PassValidation_When_ProvidedValidCreateWashingMachineDetailRequest(SaveWashingMachineDetailRequest dto, String propertyName, Object validValue) throws Exception {
		// GIVEN
		SaveWashingMachineRequest request = TestData.createSaveWashingMachineRequest().toBuilder().saveWashingMachineDetailRequest(dto).build();

		// WHEN
		ResultActions resultActions = performRequest(request);

		// THEN
		resultActions.andExpect(status().isCreated());
	}

	private ResultActions performRequest(SaveWashingMachineRequest request) throws Exception {
		String jsonRequest = jackson.writeValueAsString(request);
		return mockMvc.perform(
				multipart("/api/v1/washing-machines/save")
						.file(new MockMultipartFile( // avoids error Content-Type 'application/octet-stream' is not supported
								"saveWashingMachineRequest",
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