package org.personal.washingmachine.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.personal.washingmachine.service.WashingMachineService;

@ExtendWith(MockitoExtension.class)
class WashingMachineServiceTest {

    @InjectMocks
    private WashingMachineService washingMachineService;

    @Mock
    private WashingMachineRepository washingMachineRepositoryMock;


}