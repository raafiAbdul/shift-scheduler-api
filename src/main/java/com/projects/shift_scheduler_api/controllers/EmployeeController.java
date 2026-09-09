package com.projects.shift_scheduler_api.controllers;

import com.projects.shift_scheduler_api.dtos.*;
import com.projects.shift_scheduler_api.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDto registerDto) {
        HttpStatus status = HttpStatus.CREATED;
        WrapperDto<BasicEmployeeDetailsDto> wrapperDto = new WrapperDto<>(
                employeeService.registerEmployee(registerDto),
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }

    @PostMapping("/login")
    public void login(@RequestBody @Valid LoginDto loginDto) {

    }

    @GetMapping("/{page}")
    public ResponseEntity<?> getAllEmployees(@PathVariable Integer page,
                                             @RequestParam(required = false) Integer size ) {
        HttpStatus status = HttpStatus.OK;
        WrapperDto<Page<BasicEmployeeDetailsDto>> wrapperDto =
                new WrapperDto<>(
                        employeeService.findAllBasic(page, size),
                        status.value(),
                        status.getReasonPhrase()
                );
        return ResponseEntity.status(status).body(wrapperDto);
    }

    @GetMapping("/username")
    public ResponseEntity<?> getByUsername(@RequestParam(required = false) String username) {
        HttpStatus status = HttpStatus.OK;
        WrapperDto<BasicEmployeeDetailsDto> wrapperDto = new WrapperDto<>(
                employeeService.findByUsername(username),
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }

    @GetMapping("/email")
    public ResponseEntity<?> getByEmail(@RequestParam(required = false) String email) {
        HttpStatus status = HttpStatus.OK;
        WrapperDto<BasicEmployeeDetailsDto> wrapperDto = new WrapperDto<>(
                employeeService.findByEmail(email),
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }


    @GetMapping("/role/{page}")
    public ResponseEntity<?> getByRole(@RequestParam(required = false) String role,
                                       @PathVariable Integer page,
                                       @RequestParam(required = false) Integer size) {
        HttpStatus status = HttpStatus.OK;
        WrapperDto<Page<BasicEmployeeDetailsDto>> wrapperDto = new WrapperDto<>(
                employeeService.findByRole(role, size, page),
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }

    // change when you implement security (make is so users get themselves)
    @GetMapping("/me")
    public ResponseEntity<?> getMeById(Long id) {
        HttpStatus status = HttpStatus.OK;
        WrapperDto<BasicEmployeeDetailsDto> wrapperDto = new WrapperDto<>(
                employeeService.findById(id),
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        HttpStatus status = HttpStatus.OK;
        WrapperDto<BasicEmployeeDetailsDto> wrapperDto = new WrapperDto<>(
                employeeService.findById(id),
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }

    // change when you implement security (make is so users get themselves)
    @PutMapping("/contact/me")
    public ResponseEntity<?> updateMyContact(@RequestBody UpdateEmployeeContactDto updateDto) {
        Long id = 0L;
        HttpStatus status = HttpStatus.OK;
        WrapperDto<BasicEmployeeDetailsDto> wrapperDto = new WrapperDto<>(
                employeeService.updateEmployeeContact(updateDto, id),
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }

    @PutMapping("/worker/{id}")
    public ResponseEntity<?> updateWorkerNonContact(@RequestBody UpdateWorkerNonContactDto updateDto,
                                          @PathVariable Long id) {
        HttpStatus status = HttpStatus.OK;
        WrapperDto<BasicEmployeeDetailsDto> wrapperDto = new WrapperDto<>(
                employeeService.updateWorkerNonContact(updateDto, id),
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }

    @PutMapping("/manager/{id}")
    public ResponseEntity<?> updateManagerNonContact(@RequestBody UpdateManagerNonContactDto updateDto,
                                          @PathVariable Long id) {
        HttpStatus status = HttpStatus.OK;
        WrapperDto<BasicEmployeeDetailsDto> wrapperDto = new WrapperDto<>(
                employeeService.updateManagerNonContact(updateDto, id),
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }
}
