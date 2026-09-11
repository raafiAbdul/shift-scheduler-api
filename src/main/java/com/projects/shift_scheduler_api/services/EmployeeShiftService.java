package com.projects.shift_scheduler_api.services;

import com.projects.shift_scheduler_api.dtos.ShiftDateDto;
import com.projects.shift_scheduler_api.models.EmployeeShift;
import com.projects.shift_scheduler_api.models.EmployeeShiftKey;
import com.projects.shift_scheduler_api.repositories.EmployeeShiftRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.NoSuchElementException;

@Service
public class EmployeeShiftService {

    private final EmployeeShiftRepository employeeShiftRepository;

    public EmployeeShiftService(EmployeeShiftRepository employeeShiftRepository) {
        this.employeeShiftRepository = employeeShiftRepository;
    }

    @Transactional
    public void clockIn(Long shiftId, Long employeeId) {
        if(shiftId == null || employeeId == null)
            throw new IllegalArgumentException("ID/s cannot be null");
        if(shiftId <= 0 || employeeId <= 0)
            throw new IllegalArgumentException("ID/s cannot be negative");

        EmployeeShift es = employeeShiftRepository.findById(new EmployeeShiftKey(shiftId, employeeId))
                .orElseThrow(() -> new NoSuchElementException("This employee has not taken this shift " +
                        "or either employee/shift does not exist"));

        es.setClockedIn(OffsetDateTime.now());
    }

    @Transactional
    public void clockOut(Long shiftId, Long employeeId) {

        OffsetDateTime now = OffsetDateTime.now();

        if(shiftId == null || employeeId == null)
            throw new IllegalArgumentException("ID/s cannot be null");
        if(shiftId <= 0 || employeeId <= 0)
            throw new IllegalArgumentException("ID/s cannot be negative");

        EmployeeShift es = employeeShiftRepository.findById(new EmployeeShiftKey(shiftId, employeeId))
                .orElseThrow(() -> new NoSuchElementException("This employee has not taken this shift " +
                        "or either employee/shift does not exist"));

        if(es.getClockedIn() == null)
            throw new IllegalStateException("Have not clocked in yet");
        if(OffsetDateTime.now().isBefore(es.getClockedIn()))
            throw new IllegalArgumentException("Can't clock out before you clock in");

        ShiftDateDto clockedInTimeDto = mapOffsetDateTimeToShiftDateDto(es.getClockedIn());
        ShiftDateDto clockedOutTimeDto = mapOffsetDateTimeToShiftDateDto(now);
        es.setHoursWorked(ShiftDateDto.differenceInHours(clockedOutTimeDto, clockedInTimeDto));
        es.setClockedOut(now);
    }

    private ShiftDateDto mapOffsetDateTimeToShiftDateDto(OffsetDateTime offsetDateTime) {
        return new ShiftDateDto(
                offsetDateTime.getMonthValue(),
                offsetDateTime.getDayOfMonth(),
                offsetDateTime.getHour(),
                offsetDateTime.getMinute()
        );
    }

}
