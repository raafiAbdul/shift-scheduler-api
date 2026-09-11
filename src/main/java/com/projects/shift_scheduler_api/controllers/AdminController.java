package com.projects.shift_scheduler_api.controllers;

import com.projects.shift_scheduler_api.dtos.BasicEmployeeDetailsDto;
import com.projects.shift_scheduler_api.dtos.UpdateEmployeeContactDto;
import com.projects.shift_scheduler_api.dtos.WrapperDto;
import com.projects.shift_scheduler_api.models.Employee;
import com.projects.shift_scheduler_api.models.Manager;
import com.projects.shift_scheduler_api.models.Worker;
import com.projects.shift_scheduler_api.services.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
public class AdminController {

    private final EmployeeService employeeService;

    public AdminController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/worker")
    @Operation(
            summary = "Creates a worker",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    )
            }
    )
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
    @Operation(
            summary = "Creates a manager",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    )
            }
    )
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
    @Operation(
            summary = "Updates a worker",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found"
                    )
            }
    )
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
    @Operation(
            summary = "Updates a manager",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found"
                    )
            }
    )
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
    @Operation(
            summary = "Deletes the employee by their ID",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "No Content"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found"
                    )
            }
    )
    public ResponseEntity<?> deleteEmployeeById(@RequestParam Long id) {
        employeeService.deleteEmployee(id);
        HttpStatus status = HttpStatus.NO_CONTENT;
        WrapperDto<?> wrapperDto = new WrapperDto<>(
                null,
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }

    @DeleteMapping
    @Operation(
            summary = "Deletes all employees except the admin user",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "No Content"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    )
            }
    )
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
    @Operation(
            summary = "Gets the employee by their ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found"
                    )
            }
    )
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
    @Operation(
            summary = "Gets all employees",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    )
            }
    )
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
    @Operation(
            summary = "Updates the employee's contact information",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found"
                    )
            }
    )
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
