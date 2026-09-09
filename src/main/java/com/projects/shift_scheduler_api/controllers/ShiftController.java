package com.projects.shift_scheduler_api.controllers;

import com.projects.shift_scheduler_api.dtos.CreateOrUpdateShiftDto;
import com.projects.shift_scheduler_api.dtos.ShiftDto;
import com.projects.shift_scheduler_api.dtos.StartAndEndTimeDto;
import com.projects.shift_scheduler_api.dtos.WrapperDto;
import com.projects.shift_scheduler_api.services.EmployeeShiftService;
import com.projects.shift_scheduler_api.services.ShiftService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shift")
public class ShiftController {

    private final ShiftService shiftService;
    private final EmployeeShiftService employeeShiftService;

    public ShiftController(ShiftService shiftService, EmployeeShiftService employeeShiftService) {
        this.shiftService = shiftService;
        this.employeeShiftService = employeeShiftService;
    }

    @PostMapping("/add-employee")
    public ResponseEntity<?> addEmployee(@RequestParam Long shiftId,
                                         @RequestParam Long employeeId) {
        HttpStatus status = HttpStatus.OK;
        WrapperDto<ShiftDto> wrapperDto = new WrapperDto<>(
                shiftService.addEmployeeToShift(employeeId, shiftId),
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }

    @DeleteMapping("/remove-employee")
    public ResponseEntity<?> removeEmployee(@RequestParam Long shiftId,
                                            @RequestParam Long employeeId) {
        shiftService.removeEmployeeFromShift(employeeId, shiftId);
        HttpStatus status = HttpStatus.NO_CONTENT;
        WrapperDto<Void> wrapperDto = new WrapperDto<>(
                null,
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid CreateOrUpdateShiftDto createDto) {
        HttpStatus status = HttpStatus.CREATED;
        WrapperDto<ShiftDto> wrapperDto = new WrapperDto<>(
                shiftService.createShift(createDto),
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestParam(required = false) Long id,
            @RequestBody @Valid CreateOrUpdateShiftDto updateDto) {
        HttpStatus status = HttpStatus.OK;
        WrapperDto<ShiftDto> wrapperDto = new WrapperDto<>(
                shiftService.updateShift(id, updateDto),
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteById(@RequestParam(required = false) Long id) {
        shiftService.deleteShift(id);
        HttpStatus status = HttpStatus.NO_CONTENT;
        WrapperDto<Void> wrapperDto = new WrapperDto<>(
                null, status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        HttpStatus status = HttpStatus.OK;
        WrapperDto<ShiftDto> wrapperDto = new WrapperDto<>(
                shiftService.findById(id),
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }

    @GetMapping("/description/{page}")
    public ResponseEntity<?> getByDescription(@RequestParam(required = false) String description,
                                              @PathVariable Integer page,
                                              @RequestParam(required = false) Integer size) {
        HttpStatus status = HttpStatus.OK;
        WrapperDto<Page<ShiftDto>> wrapperDto = new WrapperDto<>(
                shiftService.findByDescription(description, size, page),
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }

    @GetMapping("/username/{page}")
    public ResponseEntity<?> getByUsername(@RequestParam(required = false) String username,
                                              @PathVariable Integer page,
                                              @RequestParam(required = false) Integer size) {
        HttpStatus status = HttpStatus.OK;
        WrapperDto<Page<ShiftDto>> wrapperDto = new WrapperDto<>(
                shiftService.findByUser(username, size, page),
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }

    @GetMapping("/time-in-between/{page}")
    public ResponseEntity<?> getByTimeInBetween(@RequestBody @Valid StartAndEndTimeDto shiftDates,
                                                @PathVariable Integer page,
                                                @RequestParam(required = false) Integer size) {
        HttpStatus status = HttpStatus.OK;
        WrapperDto<Page<ShiftDto>> wrapperDto = new WrapperDto<>(
                shiftService.findByTimeInBetween(shiftDates, size, page),
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }

    @PutMapping("/clock-in")
    public ResponseEntity<?> clockIn(@RequestParam Long shiftId,
                                     @RequestParam Long employeeId) {
        employeeShiftService.clockIn(shiftId, employeeId);
        HttpStatus status = HttpStatus.OK;
        WrapperDto<Void> wrapperDto = new WrapperDto<>(
                null, status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }

    @PutMapping("/clock-out")
    public ResponseEntity<?> clockOut(@RequestParam Long shiftId,
                                     @RequestParam Long employeeId) {
        employeeShiftService.clockOut(shiftId, employeeId);
        HttpStatus status = HttpStatus.OK;
        WrapperDto<Void> wrapperDto = new WrapperDto<>(
                null, status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }
}
