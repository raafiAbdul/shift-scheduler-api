package com.projects.shift_scheduler_api.repositories;

import com.projects.shift_scheduler_api.models.EmployeeShift;
import com.projects.shift_scheduler_api.models.EmployeeShiftKey;
import com.projects.shift_scheduler_api.models.ShiftState;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeShiftRepositoryTest extends InitializeRepositoryTest {
    @Test
    @Transactional
    void findByIdTest() {
        EmployeeShift es = employeeShiftRepository.findById(new EmployeeShiftKey(3L, 4L))
                        .orElseThrow(() -> new NoSuchElementException("Does not exist"));
        s3.setState(ShiftState.IN_PROGRESS);
        shiftRepository.save(s3);
        assertAll(
                () -> assertEquals("david_w", es.getEmployee().getUsername()),
                () -> assertEquals(ShiftState.IN_PROGRESS, es.getShift().getState()),
                () -> assertThrows(NoSuchElementException.class, () ->
                        employeeShiftRepository.findById(new EmployeeShiftKey(99L, 99L))
                                .orElseThrow(() -> new NoSuchElementException("Does not exist")))
        );
    }
}