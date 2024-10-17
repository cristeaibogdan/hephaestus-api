package org.personal.washingmachine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.personal.shared.clients.ProductClient;
import org.personal.washingmachine.dto.CreateWashingMachineRequest;
import org.personal.washingmachine.dto.WashingMachineDetailDTO;
import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.ReturnType;
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

	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper jackson;

	@MockBean
	WashingMachineService service;
	@MockBean
	WashingMachineRepository repository;
	@MockBean
	WashingMachineDamageCalculator damageCalculator;
	@MockBean
	WashingMachineReportGenerator reportGenerator;
	@MockBean
	ProductClient productClient; //TODO: To be deleted

	private final CreateWashingMachineRequest createWashingMachineRequest = new CreateWashingMachineRequest(
			"Washing Machine",
			IdentificationMode.DATA_MATRIX,
			"WhirlPool",
			"model100",
			"type200",
			"serialNumber",
			ReturnType.SERVICE,
			DamageType.IN_USE,
			null
	);

	private static final WashingMachineDetailDTO DEFAULT_DTO = new WashingMachineDetailDTO(
			false,
			false,
			false,
			false,
			false,
			false,
			0,
			false,
			0,
			false,
			"",
			false,
			"",
			false,
			false,
			0,
			false,
			0,
			false,
			"",
			false,
			"",
			0,
			0
	);


	static Stream<Arguments> getInvalidWashingMachineDetailTestCases() {
		return Stream.of(
				arguments(DEFAULT_DTO.toBuilder().visibleSurfacesScratchesLength(-1).build(), "visibleSurfacesScratchesLength", -1),
				arguments(DEFAULT_DTO.toBuilder().visibleSurfacesScratchesLength(11).build(), "visibleSurfacesScratchesLength", 11),
				arguments(DEFAULT_DTO.toBuilder().visibleSurfacesDentsDepth(-1).build(), "visibleSurfacesDentsDepth", -1),
				arguments(DEFAULT_DTO.toBuilder().visibleSurfacesDentsDepth(11).build(), "visibleSurfacesDentsDepth", 11),
				arguments(DEFAULT_DTO.toBuilder().visibleSurfacesMinorDamage(null).build(), "visibleSurfacesMinorDamage", null),
				arguments(DEFAULT_DTO.toBuilder().visibleSurfacesMinorDamage("A".repeat(201)).build(), "visibleSurfacesMinorDamage", "201 chars"),
				arguments(DEFAULT_DTO.toBuilder().visibleSurfacesMajorDamage(null).build(), "visibleSurfacesMajorDamage", null),
				arguments(DEFAULT_DTO.toBuilder().visibleSurfacesMajorDamage("A".repeat(201)).build(), "visibleSurfacesMajorDamage", "201 chars"),
				arguments(DEFAULT_DTO.toBuilder().hiddenSurfacesScratchesLength(-1).build(), "hiddenSurfacesScratchesLength", -1),
				arguments(DEFAULT_DTO.toBuilder().hiddenSurfacesScratchesLength(11).build(), "hiddenSurfacesScratchesLength", 11),
				arguments(DEFAULT_DTO.toBuilder().hiddenSurfacesDentsDepth(-1).build(), "hiddenSurfacesDentsDepth", -1),
				arguments(DEFAULT_DTO.toBuilder().hiddenSurfacesDentsDepth(11).build(), "hiddenSurfacesDentsDepth", 11),
				arguments(DEFAULT_DTO.toBuilder().hiddenSurfacesMinorDamage(null).build(), "hiddenSurfacesMinorDamage", null),
				arguments(DEFAULT_DTO.toBuilder().hiddenSurfacesMinorDamage("A".repeat(201)).build(), "hiddenSurfacesMinorDamage", "201 chars"),
				arguments(DEFAULT_DTO.toBuilder().hiddenSurfacesMajorDamage(null).build(), "hiddenSurfacesMajorDamage", null),
				arguments(DEFAULT_DTO.toBuilder().hiddenSurfacesMajorDamage("A".repeat(201)).build(), "hiddenSurfacesMajorDamage", "201 chars"),
				arguments(DEFAULT_DTO.toBuilder().price(-1).build(), "price", -1),
				arguments(DEFAULT_DTO.toBuilder().repairPrice(-1).build(), "repairPrice", -1)
		);
	}

	@ParameterizedTest(name = "Validation fails for property {1}, with value {2}")
	@MethodSource("getInvalidWashingMachineDetailTestCases")
	void should_ThrowValidationException_When_providedInvalidDTO(WashingMachineDetailDTO dto, String propertyName, Object invalidValue) throws Exception {
		// GIVEN
		CreateWashingMachineRequest request = this.createWashingMachineRequest.toBuilder().washingMachineDetailDTO(dto).build();
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

	static Stream<Arguments> getValidWashingMachineDetailTestCases() {
		return Stream.of(
				arguments(DEFAULT_DTO.toBuilder().visibleSurfacesScratchesLength(0).build(), "visibleSurfacesScratchesLength", 0),
				arguments(DEFAULT_DTO.toBuilder().visibleSurfacesScratchesLength(10).build(), "visibleSurfacesScratchesLength", 10),
				arguments(DEFAULT_DTO.toBuilder().visibleSurfacesDentsDepth(0).build(), "visibleSurfacesDentsDepth", 0),
				arguments(DEFAULT_DTO.toBuilder().visibleSurfacesDentsDepth(10).build(), "visibleSurfacesDentsDepth", 10),
				arguments(DEFAULT_DTO.toBuilder().visibleSurfacesMinorDamage("B".repeat(200)).build(), "visibleSurfacesMinorDamage", "200 chars"),
				arguments(DEFAULT_DTO.toBuilder().visibleSurfacesMajorDamage("B".repeat(200)).build(), "visibleSurfacesMajorDamage", "200 chars"),
				arguments(DEFAULT_DTO.toBuilder().hiddenSurfacesScratchesLength(0).build(), "hiddenSurfacesScratchesLength", 0),
				arguments(DEFAULT_DTO.toBuilder().hiddenSurfacesScratchesLength(10).build(), "hiddenSurfacesScratchesLength", 10),
				arguments(DEFAULT_DTO.toBuilder().hiddenSurfacesDentsDepth(0).build(), "hiddenSurfacesDentsDepth", 0),
				arguments(DEFAULT_DTO.toBuilder().hiddenSurfacesDentsDepth(10).build(), "hiddenSurfacesDentsDepth", 10),
				arguments(DEFAULT_DTO.toBuilder().hiddenSurfacesMinorDamage("B".repeat(200)).build(), "hiddenSurfacesMinorDamage", "200 chars"),
				arguments(DEFAULT_DTO.toBuilder().hiddenSurfacesMajorDamage("B".repeat(200)).build(), "hiddenSurfacesMajorDamage", "200 chars"),
				arguments(DEFAULT_DTO.toBuilder().price(0).build(), "price", 0),
				arguments(DEFAULT_DTO.toBuilder().repairPrice(0).build(), "repairPrice", 0)
		);
	}

	@ParameterizedTest(name = "Validation passes for property {1}, with value {2}")
	@MethodSource("getValidWashingMachineDetailTestCases")
	void should_PassValidation_When_providedValidDTO(WashingMachineDetailDTO dto, String propertyName, Object validValue) throws Exception {
		// GIVEN
		CreateWashingMachineRequest request = this.createWashingMachineRequest.toBuilder().washingMachineDetailDTO(dto).build();
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