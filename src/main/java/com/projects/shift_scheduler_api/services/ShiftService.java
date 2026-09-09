package com.projects.shift_scheduler_api.services;

import com.projects.shift_scheduler_api.dtos.CreateOrUpdateShiftDto;
import com.projects.shift_scheduler_api.dtos.ShiftDateDto;
import com.projects.shift_scheduler_api.dtos.ShiftDto;
import com.projects.shift_scheduler_api.dtos.StartAndEndTimeDto;
import com.projects.shift_scheduler_api.models.Employee;
import com.projects.shift_scheduler_api.models.Shift;
import com.projects.shift_scheduler_api.models.ShiftState;
import com.projects.shift_scheduler_api.repositories.EmployeeRepository;
import com.projects.shift_scheduler_api.repositories.ShiftRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.NoSuchElementException;

@Service
public class ShiftService {

    private final EmployeeRepository employeeRepository;
    private final ShiftRepository shiftRepository;

    public ShiftService(EmployeeRepository employeeRepository,
                        ShiftRepository shiftRepository) {
        this.employeeRepository = employeeRepository;
        this.shiftRepository = shiftRepository;
    }

    @Transactional
    public ShiftDto addEmployeeToShift(Long employeeId, Long shiftId) {
        if(employeeId == null || shiftId == null || employeeId < 0 || shiftId < 0)
            throw new IllegalArgumentException("Null or invalid shift or employee Id");

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
                new NoSuchElementException("No user with id #" + employeeId));

        Shift shift = shiftRepository.findById(shiftId).orElseThrow(() ->
                new NoSuchElementException("No shift with id #" + shiftId));

        shift.addEmployee(employee);
        employeeRepository.save(employee);
        shiftRepository.save(shift);
        return mapShiftToShiftDto(shift);
    }

    @Transactional
    public void removeEmployeeFromShift(Long employeeId, Long shiftId) {
        if(employeeId == null || shiftId == null || employeeId < 0 || shiftId < 0)
            throw new IllegalArgumentException("Null or invalid shift or employee Id");

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
                new NoSuchElementException("No user with id #" + employeeId));

        Shift shift = shiftRepository.findById(shiftId).orElseThrow(() ->
                new NoSuchElementException("No shift with id #" + shiftId));

        shift.removeEmployee(employee);
        employeeRepository.save(employee);
        shiftRepository.save(shift);
    }

    @Transactional
    public ShiftDto createShift(@Valid CreateOrUpdateShiftDto createShiftDto) {
        OffsetDateTime startTime = mapShiftDateDtoToOffsetDateTime(createShiftDto.getStartTime());
        OffsetDateTime endTime = mapShiftDateDtoToOffsetDateTime(createShiftDto.getEndTime());
        ShiftDateDto startTimeDto = createShiftDto.getStartTime(),
                endTimeDto = createShiftDto.getEndTime();

        if(ShiftDateDto.differenceInHours(endTimeDto, startTimeDto) > 96.00) {
            throw new IllegalArgumentException("Cannot have shift longer than 96 hours");
        }

        if(startTime.isAfter(endTime))
            throw new IllegalStateException("Can't start shift after it's ended");

        Shift newShift = mapCreateUpdateDtoToShift(createShiftDto);
        return mapShiftToShiftDto(shiftRepository.save(newShift));
    }

    @Transactional
    public ShiftDto updateShift(Long id, @Valid CreateOrUpdateShiftDto updateShiftDto) {
        if(id == null || id < 0)
            throw new IllegalArgumentException("Null or invalid Id");

        Shift s = shiftRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("No shift with id #" + id));

        if(s.getState() == ShiftState.CLOSED)
            throw new IllegalStateException("Shift has already closed");

        if(ShiftDateDto.differenceInHours(updateShiftDto.getEndTime(), updateShiftDto.getStartTime()) > 96.00) {
            throw new IllegalArgumentException("Cannot have shift longer than 96 hours");
        }

        OffsetDateTime startTime = mapShiftDateDtoToOffsetDateTime(updateShiftDto.getStartTime());
        OffsetDateTime endTime = mapShiftDateDtoToOffsetDateTime(updateShiftDto.getEndTime());

        if(startTime.isAfter(endTime))
            throw new IllegalStateException("Can't start shift after it's ended");

        s.setRequiredEmployeesCount(updateShiftDto.getRequiredEmployeeCount());
        s.setDescription(updateShiftDto.getDescription());
        s.setStartTime(startTime);
        s.setEndTime(endTime);
        s.setState(updateShiftDto.getState());

        return mapShiftToShiftDto(shiftRepository.save(s));
    }

    @Transactional
    public void deleteShift(Long id) {
        if(id == null || id < 0)
            throw new IllegalArgumentException("Null or invalid Id");

        shiftRepository.deleteById(id);
    }

    @Transactional
    public void deleteBulk() {
        shiftRepository.deleteBulk();
    }

    public ShiftDto findById(Long id) {
        if(id == null || id < 0)
            throw new IllegalArgumentException("Null or invalid Id");

        Shift shift = shiftRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("No shift with id #" + id));

        return mapShiftToShiftDto(shift);
    }

    public Page<ShiftDto> findByDescription(String description, Integer size, Integer page) {
        size = (size == null || size <= 0) ? 10 : size;
        page = (page == null || page < 0) ? 0 : page;

        if(description == null || description.trim().isEmpty())
            throw new IllegalArgumentException("Null or empty description");

        Page<Shift> shiftPage = shiftRepository.findByDescription(description, PageRequest.of(page, size));

        return shiftPage.map(this::mapShiftToShiftDto);

    }

    public Page<ShiftDto> findByUser(String username, Integer size, Integer page) {
        size = (size == null || size <= 0) ? 10 : size;
        page = (page == null || page < 0) ? 0 : page;

        if(username == null || username.trim().isEmpty())
            throw new IllegalArgumentException("Null or empty username");

        Page<Shift> shiftPage = shiftRepository.findByUser(username, PageRequest.of(page, size));
        return shiftPage.map(this::mapShiftToShiftDto);
    }

    public Page<ShiftDto> findByTimeInBetween(@Valid StartAndEndTimeDto startAndEnd, Integer size, Integer page) {
        size = (size == null || size <= 0) ? 10 : size;
        page = (page == null || page < 0) ? 0 : page;

        OffsetDateTime startTime = mapShiftDateDtoToOffsetDateTime(startAndEnd.getStartTime()),
                endTime = mapShiftDateDtoToOffsetDateTime(startAndEnd.getEndTime());

        if(startTime.isAfter(endTime))
            throw new IllegalStateException("Start time must be before end time");

        Page<Shift> shiftPage = shiftRepository.findByTimeInBetween(startTime, endTime,
                PageRequest.of(page, size));

        return shiftPage.map(this::mapShiftToShiftDto);
    }

    private ShiftDto mapShiftToShiftDto(Shift shift) {
        return new ShiftDto(shift.getId(),
                shift.getDescription(),
                shift.getRequiredEmployeesCount(),
                shift.getStartTime(),
                shift.getEndTime(),
                shift.getState(),
                shift.getCurrentEmployeeCount(),
                shift.getEmployeeShifts());
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

    private Shift mapCreateUpdateDtoToShift(CreateOrUpdateShiftDto createOrUpdateShiftDto) {
        Shift s = new Shift();
        s.setRequiredEmployeesCount(createOrUpdateShiftDto.getRequiredEmployeeCount());
        s.setDescription(createOrUpdateShiftDto.getDescription());
        s.setStartTime(
                mapShiftDateDtoToOffsetDateTime(createOrUpdateShiftDto.getStartTime())
        );
        s.setEndTime(
                mapShiftDateDtoToOffsetDateTime(createOrUpdateShiftDto.getEndTime())
        );
        s.setState(createOrUpdateShiftDto.getState());

        return s;
    }

}
