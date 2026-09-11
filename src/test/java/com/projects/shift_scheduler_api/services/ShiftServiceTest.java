package com.projects.shift_scheduler_api.services;

import com.projects.shift_scheduler_api.dtos.ShiftDateDto;
import com.projects.shift_scheduler_api.models.ShiftState;
import com.projects.shift_scheduler_api.repositories.EmployeeRepository;
import com.projects.shift_scheduler_api.repositories.ShiftRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShiftServiceTest extends InitializeServiceTest {
    @Mock
    EmployeeRepository employeeRepository;
    @Mock
    ShiftRepository shiftRepository;

    @InjectMocks
    ShiftService shiftService;

    @Test
    void addEmployeeToShift_happyFlow() {
        Long employeeId = 1L, shiftId = 1L;

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(a1));
        when(shiftRepository.findById(shiftId)).thenReturn(Optional.of(s1));

        shiftService.addEmployeeToShift(employeeId, shiftId);

        verify(employeeRepository, times(1))
                .save(a1);
        verify(shiftRepository, times(1))
                .save(s1);

        verify(employeeRepository, times(1))
                .findById(employeeId);
        verify(shiftRepository, times(1))
                .findById(shiftId);
    }

    @Test
    void addEmployeeToShift_throwsIllegalArgumentException() {
        Long employeeId = null, shiftId = 1L;
        assertThrows(IllegalArgumentException.class, () ->
            shiftService.addEmployeeToShift(employeeId, shiftId));
        Long employeeId2 = -2L, shiftId2 = 1L;
        assertThrows(IllegalArgumentException.class, () ->
            shiftService.addEmployeeToShift(employeeId2, shiftId2));
    }

    @Test
    void addEmployeeToShift_throwsNoSuchElementException() {
        Long employeeId = 2L, shiftId = 1L;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () ->
            shiftService.addEmployeeToShift(employeeId, shiftId));
    }

    @Test
    void removeEmployeeFromShift_happyFlow() {
        Long employeeId = 2L, shiftId = 1L;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(m1));
        when(shiftRepository.findById(shiftId)).thenReturn(Optional.of(s2));

        s2.addEmployee(m1);

        shiftService.removeEmployeeFromShift(employeeId, shiftId);

        verify(employeeRepository, times(1))
                .findById(employeeId);
        verify(shiftRepository, times(1))
                .findById(shiftId);

        verify(employeeRepository, times(1))
                .save(m1);
        verify(shiftRepository, times(1))
                .save(s2);
    }

    @Test
    void removeEmployeeFromShift_throwsIllegalArgumentException() {
        Long employeeId = 2L, shiftId = -1L;

        assertThrows(IllegalArgumentException.class, () ->
            shiftService.removeEmployeeFromShift(employeeId, shiftId));

        Long employeeId2 = null, shiftId2 = 1L;

        assertThrows(IllegalArgumentException.class, () ->
            shiftService.removeEmployeeFromShift(employeeId2, shiftId2));

    }

    @Test
    void removeEmployeeFromShift_throwsNoSuchElementException() {
        Long employeeId = 2L, shiftId = 1L;

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
            shiftService.removeEmployeeFromShift(employeeId, shiftId));

    }

    @Test
    void createShift_happyFlow() {
        when(shiftRepository.save(newShift)).thenReturn(newShift);

        shiftService.createShift(createDto);

        verify(shiftRepository, times(1)).save(newShift);
    }

    @Test
    void createShift_throwsIllegalStateException() {
        createDto.setEndTime(new ShiftDateDto(6, 3, 15, 30));
        assertThrows(IllegalStateException.class, () ->
                shiftService.createShift(createDto));
        startAndEnd.setEndTime(new ShiftDateDto(9, 7, 17, 0));
    }

    @Test
    void createShift_throwsIllegalArgumentException() {
        createDto.setEndTime(new ShiftDateDto(10, 3, 15, 30));
        assertThrows(IllegalArgumentException.class, () ->
                shiftService.createShift(createDto));
        startAndEnd.setEndTime(new ShiftDateDto(9, 7, 17, 0));
    }

    @Test
    void updateShift_throwsIllegalStateExceptions() {
        Long id = 2L;
        when(shiftRepository.findById(id)).thenReturn(Optional.of(newShift));

        assertAll(
                // throws "Can't start shift after it's ended"
                () -> assertThrows(IllegalStateException.class, () -> {
                            createDto.setEndTime(new ShiftDateDto(6, 3, 15, 30));
                            shiftService.updateShift(id, createDto);
                        }),

                // throws "Shift has already closed"
                () -> assertThrows(IllegalStateException.class, () -> {
                        newShift.setState(ShiftState.CLOSED);
                        shiftService.updateShift(id, createDto);
                        createDto.setState(ShiftState.UPCOMING);
                })
        );
        startAndEnd.setEndTime(new ShiftDateDto(9, 7, 17, 0));
    }

    @Test
    void updateShift_ThrowsIllegalArgumentException() {
        createDto.setEndTime(new ShiftDateDto(10, 3, 15, 30));
        createDto.setState(ShiftState.UPCOMING);
        Long id = -2L, id2 = 2L;

        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () ->
                        shiftService.updateShift(id, createDto)),
                () -> assertThrows(IllegalArgumentException.class, () -> {
                        when(shiftRepository.findById(id2)).thenReturn(Optional.of(newShift));
                        shiftService.updateShift(id2, createDto);
                })
        );
        startAndEnd.setEndTime(new ShiftDateDto(9, 7, 17, 0));
    }

    @Test
    void deleteShift_happyFlow() {
        Long id = 1L;
        shiftService.deleteShift(id);
        verify(shiftRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteShift_errorFlow() {
        Long id = -1L;
        assertThrows(IllegalArgumentException.class, () ->
            shiftService.deleteShift(id));
    }

    @Test
    void deleteBulkTest() {
        shiftService.deleteBulk();
        verify(shiftRepository, times(1)).deleteBulk();
    }

    @Test
    void findByDescription_happyFlow() {
        int page = 2, size = 20;
        String description = "yes";
        when(shiftRepository.findByDescription(description, PageRequest.of(page, size)))
                .thenReturn(new PageImpl<>(List.of(s3)));

        shiftService.findByDescription(description, size, page);

        verify(shiftRepository, times(1))
                .findByDescription(description, PageRequest.of(page, size));

    }

    @Test
    void findByDescription_throwsIllegalArgumentException() {
        Integer page = null, size = -1;
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () ->
                    shiftService.findByDescription(null, size, page)),
                () -> assertThrows(IllegalArgumentException.class, () ->
                    shiftService.findByDescription(" ", size, page))
        );
    }

    @Test
    void findByUser_happyFlow() {
        int page = 2, size = 20;
        String description = "jake_w";
        when(shiftRepository.findByUser(description, PageRequest.of(page, size)))
                .thenReturn(new PageImpl<>(List.of(s3)));

        shiftService.findByUser(description, size, page);

        verify(shiftRepository, times(1))
                .findByUser(description, PageRequest.of(page, size));

    }

    @Test
    void findByUser_throwsIllegalArgumentException() {
        Integer page = null, size = -1;
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () ->
                    shiftService.findByUser(null, size, page)),
                () -> assertThrows(IllegalArgumentException.class, () ->
                    shiftService.findByUser(" ", size, page))
        );
    }

    @Test
    void findByTimeInBetween_happyFlow() {
        int page = 3, size = 5;
        OffsetDateTime startTime = mapShiftDateDtoToOffsetDateTime(startAndEnd.getStartTime());
        OffsetDateTime endTime = mapShiftDateDtoToOffsetDateTime(startAndEnd.getEndTime());

        when(shiftRepository.findByTimeInBetween(startTime, endTime,
                PageRequest.of(page, size))).thenReturn(new PageImpl<>(List.of(s3)));

        shiftService.findByTimeInBetween(startAndEnd, size, page);

        verify(shiftRepository, times(1)).findByTimeInBetween(
                startTime, endTime, PageRequest.of(page, size)
        );
    }

    @Test
    void findByTimeInBetween_throwsIllegalStateException() {
        int page = 3, size = 5;
        assertThrows(IllegalStateException.class, () ->
                shiftService.findByTimeInBetween(endLargerThanStart, size, page));
    }

    private OffsetDateTime mapShiftDateDtoToOffsetDateTime(ShiftDateDto shiftDateDto) {
        return OffsetDateTime.of(
                OffsetDateTime.now().getYear(),
                shiftDateDto.getMonth(),
                shiftDateDto.getDay(),
                shiftDateDto.getHour(),
                shiftDateDto.getMinute(),
                0, 0,
                ZoneOffset.UTC
        );
    }
}