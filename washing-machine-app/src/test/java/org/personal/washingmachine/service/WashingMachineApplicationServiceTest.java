package org.personal.washingmachine.service;

import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.personal.shared.clients.ProductClient;
import org.personal.washingmachine.WashingMachineTestData;
import org.personal.washingmachine.dto.PageRequestDTO;
import org.personal.washingmachine.dto.WashingMachineDetailDTO;
import org.personal.washingmachine.dto.WashingMachineSimpleDTO;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.entity.WashingMachineDetail;
import org.personal.washingmachine.entity.embedded.HiddenSurfaceDamage;
import org.personal.washingmachine.entity.embedded.PackageDamage;
import org.personal.washingmachine.entity.embedded.VisibleSurfaceDamage;
import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.enums.ReturnType;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.personal.washingmachine.service.calculators.HiddenSurfacesRecommendationCalculator;
import org.personal.washingmachine.service.calculators.PackageRecommendationCalculator;
import org.personal.washingmachine.service.calculators.PricingRecommendationCalculator;
import org.personal.washingmachine.service.calculators.VisibleSurfacesRecommendationCalculator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.personal.washingmachine.enums.Recommendation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class WashingMachineApplicationServiceTest {
    WashingMachineRepository washingMachineRepositoryMock = mock(WashingMachineRepository.class);

    WashingMachineService washingMachineService = new WashingMachineService(washingMachineRepositoryMock);
    WashingMachineDamageCalculator washingMachineDamageCalculator = new WashingMachineDamageCalculator(
            new PackageRecommendationCalculator(),
            new VisibleSurfacesRecommendationCalculator(),
            new HiddenSurfacesRecommendationCalculator(),
            new PricingRecommendationCalculator()
    );
    WashingMachineReportGenerator washingMachineReportGenerator = new WashingMachineReportGenerator();

    ProductClient productClient; //TODO: To be deleted

    WashingMachineApplicationService underTest = new WashingMachineApplicationService(
            washingMachineService,
            washingMachineRepositoryMock,
            washingMachineDamageCalculator,
            washingMachineReportGenerator,
            productClient
    );

    @Nested
    class testLoadPaginatedAndFiltered {

        @Test
        void should_ReturnCorrectPageIndexAndSize() {
            // GIVEN
            PageRequestDTO dto = PageRequestDTO.builder()
                    .pageIndex(0)
                    .pageSize(2)
                    .build();

            Page<WashingMachine> expectedPage = new PageImpl<>(
                    List.of(),
                    PageRequest.of(dto.pageIndex(), dto.pageSize()),
                    999);

            given(washingMachineRepositoryMock.findAll(any(Predicate.class),any(PageRequest.class)))
                    .willReturn(expectedPage);

            // WHEN
            Page<WashingMachineSimpleDTO> actual = underTest.loadPaginatedAndFiltered(dto);

            // THEN
            assertThat(actual.getNumber())
                    .isEqualTo(expectedPage.getNumber());
            assertThat(actual.getSize())
                    .isEqualTo(expectedPage.getSize());
        }

        @Test
        void should_CallMethodWithGivenObjects() {
            ArgumentCaptor<Predicate> predicateArgumentCaptor = ArgumentCaptor.forClass(Predicate.class);
            ArgumentCaptor<PageRequest> pageRequestArgumentCaptor = ArgumentCaptor.forClass(PageRequest.class);

            // GIVEN
            PageRequestDTO dto = PageRequestDTO.builder()
                    .pageIndex(0)
                    .pageSize(2)
                    .build();

            Page<WashingMachine> expectedPage = new PageImpl<>(
                   List.of(),
                   PageRequest.of(dto.pageIndex(), dto.pageSize()),
                   999);

            given(washingMachineRepositoryMock.findAll(predicateArgumentCaptor.capture(),pageRequestArgumentCaptor.capture()))
                    .willReturn(expectedPage);

            // WHEN
            underTest.loadPaginatedAndFiltered(dto);

            // THEN
            verify(washingMachineRepositoryMock).findAll(predicateArgumentCaptor.getValue(), pageRequestArgumentCaptor.getValue());
        }

        @Test @Disabled
        void should_ReturnDTOsSortedByDescendingDate() {
            // GIVEN
            PageRequestDTO dto = PageRequestDTO.builder()
                    .pageIndex(0)
                    .pageSize(5)
                    .manufacturer("Bosch")
                    .build();

            List<WashingMachine> washingMachines = new ArrayList<>();
            washingMachines.add(WashingMachineTestData.getWashingMachineWithoutDetailAndImages("Bosch"));
            washingMachines.add(WashingMachineTestData.getWashingMachineWithoutDetailAndImages("Bosch"));

            Page<WashingMachine> expectedPage = new PageImpl<>(
                    washingMachines,
                    PageRequest.of(dto.pageIndex(), dto.pageSize()),
                    washingMachines.size());

            given(washingMachineRepositoryMock.findAll(any(Predicate.class),any(PageRequest.class)))
                    .willReturn(expectedPage);

            // WHEN
            Page<WashingMachineSimpleDTO> actual = underTest.loadPaginatedAndFiltered(dto);

            // THEN
            assertThat(actual.getContent())
                    .extracting(wm -> wm.manufacturer())
                    .containsExactly("Bosch", "Bosch");
        }

        @Test
        void should_ReturnPageWithTwoDTOs_When_FilteredByManufacturer() {
            // GIVEN
            PageRequestDTO dto = PageRequestDTO.builder()
                    .pageIndex(0)
                    .pageSize(2)
                    .manufacturer("Bosch")
                    .build();

            List<WashingMachine> washingMachines = new ArrayList<>();
            washingMachines.add(WashingMachineTestData.getWashingMachineWithoutDetailAndImages("Bosch"));
            washingMachines.add(WashingMachineTestData.getWashingMachineWithoutDetailAndImages("Bosch"));

            Page<WashingMachine> expectedPage = new PageImpl<>(
                    washingMachines,
                    PageRequest.of(dto.pageIndex(), dto.pageSize()),
                    washingMachines.size());

            given(washingMachineRepositoryMock.findAll(any(Predicate.class),any(PageRequest.class)))
                    .willReturn(expectedPage);

            // WHEN
            Page<WashingMachineSimpleDTO> actual = underTest.loadPaginatedAndFiltered(dto);

            // THEN
            assertThat(actual.getContent())
                    .extracting(wm -> wm.manufacturer())
                    .containsExactly("Bosch", "Bosch");
        }

        @Test
        void should_ReturnPageWithThreeDTOs_When_FilteredByIdentificationMode() {
            // GIVEN
            PageRequestDTO dto = PageRequestDTO.builder()
                    .pageIndex(0)
                    .pageSize(3)
                    .identificationMode(IdentificationMode.QR_CODE)
                    .build();

            List<WashingMachine> washingMachines = new ArrayList<>();
            washingMachines.add(WashingMachineTestData.getWashingMachineWithoutDetailAndImages(IdentificationMode.QR_CODE));
            washingMachines.add(WashingMachineTestData.getWashingMachineWithoutDetailAndImages(IdentificationMode.QR_CODE));
            washingMachines.add(WashingMachineTestData.getWashingMachineWithoutDetailAndImages(IdentificationMode.QR_CODE));

            Page<WashingMachine> expectedPage = new PageImpl<>(
                    washingMachines,
                    PageRequest.of(dto.pageIndex(), dto.pageSize()),
                    washingMachines.size());

            given(washingMachineRepositoryMock.findAll(any(Predicate.class),any(PageRequest.class)))
                    .willReturn(expectedPage);

            // WHEN
            Page<WashingMachineSimpleDTO> actual = underTest.loadPaginatedAndFiltered(dto);

            // THEN
            assertThat(actual.getContent())
                    .extracting(wm -> wm.identificationMode())
                    .containsExactly(IdentificationMode.QR_CODE, IdentificationMode.QR_CODE, IdentificationMode.QR_CODE);
        }
    }

    @Nested
    class testGetRecommendation {

        @Test
        void should_ReturnRESALE_When_PackageDirtyTrue() {
            // GIVEN
            WashingMachineDetailDTO dto = WashingMachineDetailDTO.builder()
                    .applicablePackageDamage(true)
                    .packageDirty(true)
                    .build();

            Recommendation expected = RESALE;

            // WHEN
            Recommendation actual = underTest.getRecommendation(dto);

            // THEN
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    class testGetReport {

        @Test
        void should_ReturnReport() {
            // GIVEN
            String serialNumber = "ABC-987";

            WashingMachineDetail washingMachineDetail = new WashingMachineDetail(
                    new PackageDamage(true, true, true),
                    new VisibleSurfaceDamage(
                            5.5,
                            0,
                            "some minor damage",
                            "some major damage"
                    ),
                    new HiddenSurfaceDamage(
                            5.5,
                            0,
                            "some minor damage",
                            "some major damage"
                    ),
                    0,
                    0
            );

            WashingMachine washingMachine = new WashingMachine(
                    "Washing Machine",
                    "Whirlpool",
                    DamageType.IN_USE,
                    ReturnType.SERVICE,
                    IdentificationMode.DATA_MATRIX,
                    "test",
                    "modelOne",
                    "typeOne",
                    Recommendation.RESALE,
                    washingMachineDetail
            );

            // washingMachine.addImage(); TODO: How to create images?

            given(washingMachineRepositoryMock.findBySerialNumber(serialNumber))
                    .willReturn(Optional.of(washingMachine));

            // WHEN
//            WashingMachineReportDTO report = underTest.getReport(serialNumber);

            // THEN
//            assertThat(report.createdAt())
//                    .isEqualTo(LocalDateTime.now()); TODO: This is not possible, as hibernate generated the date after saving...
        }
    }

    @Nested
    class testIsSerialNumberInUse {

        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        void should_ReturnTrue(boolean expected) {
            // GIVEN
            String serialNumber = "ABC-987";

            given(washingMachineRepositoryMock.existsBySerialNumber(serialNumber))
                    .willReturn(expected);

            // WHEN
            boolean actual = underTest.isSerialNumberInUse(serialNumber);

            // THEN
            assertThat(actual).isEqualTo(expected);
        }
    }

}