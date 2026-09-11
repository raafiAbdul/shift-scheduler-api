package com.projects.shift_scheduler_api.controllers;

import com.projects.shift_scheduler_api.dtos.BasicEmployeeDetailsDto;
import com.projects.shift_scheduler_api.dtos.UpdateEmployeeContactDto;
import com.projects.shift_scheduler_api.dtos.WrapperDto;
import com.projects.shift_scheduler_api.models.Employee;
import com.projects.shift_scheduler_api.models.Manager;
import com.projects.shift_scheduler_api.models.Worker;
import com.projects.shift_scheduler_api.services.EmployeeService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin Endpoints")
@Hidden
public class AdminController {

    private final EmployeeService employeeService;

    public AdminController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/worker")
    public ResponseEntity<?> createWorker(@RequestBody @Valid Worker w) {
        HttpStatus status = HttpStatus.CREATED;
        WrapperDto<Worker> wrapperDto = new WrapperDto<>(
                employeeService.createWorker(w),
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status)
                .body(wrapperDto);
    }

    @PostMapping("/manager")
    public ResponseEntity<?> createManager(@RequestBody @Valid Manager m) {
        HttpStatus status = HttpStatus.CREATED;
        WrapperDto<Manager> wrapperDto = new WrapperDto<>(
                employeeService.createManager(m),
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status)
                .body(wrapperDto);
    }

    @PutMapping("/update/worker")
    public ResponseEntity<?> updateWorker(@RequestBody @Valid Worker newWorker,
                                          @RequestParam Long id) {
        HttpStatus status = HttpStatus.OK;
        WrapperDto<Worker> wrapperDto = new WrapperDto<>(
                employeeService.updateWorker(id, newWorker),
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status)
                .body(wrapperDto);
    }

    @PutMapping("update/manager")
    public ResponseEntity<?> updateManager(@RequestBody @Valid Manager newManager,
                                           @RequestParam Long id) {
        HttpStatus status = HttpStatus.OK;
        WrapperDto<Manager> wrapperDto = new WrapperDto<>(
                employeeService.updateManager(id, newManager),
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status)
                .body(wrapperDto);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteEmployeeById(@RequestParam Long id) {
        HttpStatus status = HttpStatus.NO_CONTENT;
        WrapperDto<?> wrapperDto = new WrapperDto<>(
                null,
                status.value(),
                status.getReasonPhrase()
        );
        employeeService.deleteEmployee(id);
        return ResponseEntity.status(status).body(wrapperDto);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll() {
        HttpStatus status = HttpStatus.NO_CONTENT;
        WrapperDto<?> wrapperDto = new WrapperDto<>(
                null,
                status.value(),
                status.getReasonPhrase()
        );
        employeeService.deleteAll();
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

    @GetMapping("/all/{page}")
    public ResponseEntity<?> getAll(@RequestParam(required = false) Integer size,
                                    @PathVariable Integer page) {
        HttpStatus status = HttpStatus.OK;
        WrapperDto<Page<Employee>> wrapperDto = new WrapperDto<>(
                employeeService.findAll(size, page),
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }

    @PutMapping("/contact/{id}")
    public ResponseEntity<?> updateEmployeeContact(@RequestBody UpdateEmployeeContactDto updateDto,
                                                   @PathVariable Long id) {
        HttpStatus status = HttpStatus.OK;
        WrapperDto<BasicEmployeeDetailsDto> wrapperDto = new WrapperDto<>(
                employeeService.updateEmployeeContact(updateDto, id),
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }

}
