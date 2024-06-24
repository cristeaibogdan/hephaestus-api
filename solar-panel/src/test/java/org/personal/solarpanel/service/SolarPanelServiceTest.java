package org.personal.solarpanel.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.personal.solarpanel.entity.SolarPanel;
import org.personal.solarpanel.repository.SolarPanelRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

//@ExtendWith(MockitoExtension.class)
class SolarPanelServiceTest {

//    @Mock
    private SolarPanelRepository solarPanelRepositoryMock = mock(SolarPanelRepository.class);

//    @InjectMocks
    private SolarPanelService underTest = new SolarPanelService(solarPanelRepositoryMock);

    @Nested
    class TestSaveSolarPanel {

        @Test
        void should_SaveSolarPanel() {
            //GIVEN
            SolarPanel solarPanel = new SolarPanel(
                    "Solar Panel",
                    "SunPower",
                    "serialNumber");

            //WHEN
            underTest.saveSolarPanel(solarPanel);

            //THEN
            then(solarPanelRepositoryMock)
                    .should(times(1))
                    .save(solarPanel);
        }

        @Test
        void should_ThrowRuntimeException_When_SerialNumberIsTaken() {
            //GIVEN
            SolarPanel solarPanel = new SolarPanel(
                    "Solar Panel",
                    "SunPower",
                    "serialNumber");

            given(solarPanelRepositoryMock.existsBySerialNumber(solarPanel.getSerialNumber()))
                    .willReturn(true);

            //WHEN & THEN
            assertThatThrownBy(() -> underTest.saveSolarPanel(solarPanel))
                    .isInstanceOf(RuntimeException.class);
        }
    }

}