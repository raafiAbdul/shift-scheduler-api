package com.projects.shift_scheduler_api.services;

import com.projects.shift_scheduler_api.models.EmployeeShift;
import com.projects.shift_scheduler_api.models.EmployeeShiftKey;
import com.projects.shift_scheduler_api.repositories.EmployeeShiftRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeShiftServiceTest extends InitializeServiceTest {

    @Mock
    EmployeeShiftRepository employeeShiftRepository;

    @InjectMocks
    EmployeeShiftService employeeShiftService;

    static EmployeeShift employeeShift = new EmployeeShift(
            new EmployeeShiftKey(1L, 1L)
    );

    @Test
    void clockIn_happyFlow() {
        long shiftId = 1L, employeeId = 1L;
        EmployeeShiftKey esk = new EmployeeShiftKey(shiftId, employeeId);
        when(employeeShiftRepository.findById(esk))
                .thenReturn(Optional.of(employeeShift));

        employeeShiftService.clockIn(shiftId, employeeId);

        verify(employeeShiftRepository, times(1))
                .findById(esk);
    }

    @Test
    void clockIn_errorFlows() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () ->
                        employeeShiftService.clockIn(null, null)),
                () -> assertThrows(IllegalArgumentException.class, () ->
                        employeeShiftService.clockIn(-1L, -2L)),
                () -> assertThrows(NoSuchElementException.class, () -> {
                        when(employeeShiftRepository.findById(any(EmployeeShiftKey.class)))
                                .thenReturn(Optional.empty());
                        employeeShiftService.clockIn(1L, 1L);
                })
        );
    }

    @Test
    void clockOut_happyFlow() {
        OffsetDateTime now = OffsetDateTime.now().minusHours(8L);
        long shiftId = 1L, employeeId = 1L;
        EmployeeShiftKey esk = new EmployeeShiftKey(shiftId, employeeId);
        when(employeeShiftRepository.findById(esk))
                .thenReturn(Optional.of(employeeShift));

        employeeShift.setClockedIn(now);

        employeeShiftService.clockOut(shiftId, employeeId);

        verify(employeeShiftRepository, times(1))
                .findById(esk);

        assertTrue(employeeShift.getHoursWorked() <= 8 &&
                employeeShift.getHoursWorked() >= 7.5);

    }

    @Test
    void clockOut_errorFlows() {
        Long shiftId = 1L, employeeId = 1L;
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () ->
                        employeeShiftService.clockOut(null, null)),
                () -> assertThrows(IllegalArgumentException.class, () ->
                        employeeShiftService.clockOut(-1L, -2L)),
                () -> assertThrows(NoSuchElementException.class, () -> {
                    when(employeeShiftRepository.findById(any(EmployeeShiftKey.class)))
                            .thenReturn(Optional.empty());
                    employeeShiftService.clockOut(shiftId, employeeId);
                }),
                () -> assertThrows(IllegalArgumentException.class, () -> {
                    when(employeeShiftRepository.findById(any(EmployeeShiftKey.class)))
                            .thenReturn(Optional.of(employeeShift));
                    employeeShift.setClockedIn(OffsetDateTime.now().plusHours(8));
                    employeeShiftService.clockOut(shiftId, employeeId);
                }),
                () -> assertThrows(IllegalStateException.class, () -> {
                    when(employeeShiftRepository.findById(any(EmployeeShiftKey.class)))
                            .thenReturn(Optional.of(employeeShift));
                    employeeShift.setClockedIn(null);
                    employeeShiftService.clockOut(shiftId, employeeId);
                })
        );
    }


}