package org.personal.washingmachine.service;

import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
import org.personal.shared.clients.ProductClient;
import org.personal.washingmachine.WashingMachineTestData;
import org.personal.washingmachine.dto.PageRequestDTO;
import org.personal.washingmachine.dto.WashingMachineDetailsDTO;
import org.personal.washingmachine.dto.WashingMachineReportDTO;
import org.personal.washingmachine.dto.WashingMachineSimpleDTO;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.entity.WashingMachineDetails;
import org.personal.washingmachine.entity.embedded.HiddenSurfaceDamage;
import org.personal.washingmachine.entity.embedded.PackageDamage;
import org.personal.washingmachine.entity.embedded.VisibleSurfaceDamage;
import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.enums.ReturnType;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.personal.washingmachine.service.WashingMachineApplicationService;
import org.personal.washingmachine.service.calculators.HiddenSurfacesRecommendationCalculator;
import org.personal.washingmachine.service.calculators.PackageRecommendationCalculator;
import org.personal.washingmachine.service.calculators.PricingRecommendationCalculator;
import org.personal.washingmachine.service.calculators.VisibleSurfacesRecommendationCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.personal.washingmachine.entity.QWashingMachine.washingMachine;
import static org.personal.washingmachine.enums.Recommendation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        void should_ReturnTwoDTOs_When_FilteredByManufacturer() {
            // GIVEN
            PageRequestDTO dto = PageRequestDTO.builder()
                    .pageIndex(0)
                    .pageSize(2)
                    .manufacturer("Bosch")
                    .build();

            WashingMachine one = WashingMachineTestData.getWashingMachineWithoutDetailsAndWithoutImages();
            one.setManufacturer("Bosch");

            WashingMachine two = WashingMachineTestData.getWashingMachineWithoutDetailsAndWithoutImages();
            two.setManufacturer("Bosch");

            Page<WashingMachine> expected = new PageImpl<>(
                    List.of(one, two),
                    PageRequest.of(dto.pageIndex(), dto.pageSize()),
                    2);

            given(washingMachineRepositoryMock.findAll(any(Predicate.class),any(PageRequest.class)))
                    .willReturn(expected);

            // WHEN
            Page<WashingMachineSimpleDTO> actual = underTest.loadPaginatedAndFiltered(dto);

            // THEN
            assertThat(actual.getNumber()).isEqualTo(0);
            assertThat(actual.getSize()).isEqualTo(2);
//            assertThat(actual.getContent()).isEqualTo(List.of(
//                    one, two
//            ));
        }
    }

    @Nested
    class testGetRecommendation {

        @Test
        void should_ReturnRESALE_When_PackageDirtyTrue() {
            // GIVEN
            WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
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

            WashingMachineDetails washingMachineDetails = new WashingMachineDetails(
                    new PackageDamage(true, true, true, true),
                    new VisibleSurfaceDamage(
                            true,
                            true,
                            5.5,
                            false,
                            0,
                            true,
                            "some minor damage",
                            true,
                            "some major damage"
                    ),
                    new HiddenSurfaceDamage(
                            true,
                            true,
                            5.5,
                            false,
                            0,
                            true,
                            "some minor damage",
                            true,
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
                    washingMachineDetails
            );

            // washingMachine.addImage(); TODO: How to create images?

            given(washingMachineRepositoryMock.findBySerialNumber(serialNumber))
                    .willReturn(Optional.of(washingMachine));

            // WHEN
            WashingMachineReportDTO report = underTest.getReport(serialNumber);

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