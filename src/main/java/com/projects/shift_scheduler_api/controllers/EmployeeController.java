package com.projects.shift_scheduler_api.controllers;

import com.projects.shift_scheduler_api.dtos.*;
import com.projects.shift_scheduler_api.security.models.EmployeeDetails;
import com.projects.shift_scheduler_api.services.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/employee")
@Tag(name = "Employee Related Endpoints")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/register")
    @Operation(
            summary = "Registers a new employee",
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
                            responseCode = "404",
                            description = "Not Found"
                    )
            }
    )
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
    @Operation(
            summary = "Logs in an employee",
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
                            responseCode = "401",
                            description = "Unauthorized"
                    )
            }
    )
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto) {
        HttpStatus status = HttpStatus.OK;
        WrapperDto<Map<String, String>> wrapperDto = new WrapperDto<>(
                Collections.singletonMap("JWT", employeeService.verify(loginDto)),
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }

    @PostMapping("/logout")
    @Operation(
            summary = "Returns 200 OK status to signal it's ok to throw bearer token",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    )
            }
    )
    public ResponseEntity<?> logout() {
        HttpStatus status = HttpStatus.OK;
        WrapperDto<Map<String, String>> wrapperDto = new WrapperDto<>(
                Collections.singletonMap("message", "You've successfully logged in"),
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('USER_READ')")
    @GetMapping("/{page}")
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

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('USER_READ')")
    @GetMapping("/username")
    @Operation(
            summary = "Gets employee by username",
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
                            responseCode = "404",
                            description = "Not Found"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    )
            }
    )
    public ResponseEntity<?> getByUsername(@RequestParam(required = false) String username) {
        HttpStatus status = HttpStatus.OK;
        WrapperDto<BasicEmployeeDetailsDto> wrapperDto = new WrapperDto<>(
                employeeService.findByUsername(username),
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('USER_READ')")
    @GetMapping("/email")
    @Operation(
            summary = "Gets employee by email",
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
                            responseCode = "404",
                            description = "Not Found"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    )
            }
    )
    public ResponseEntity<?> getByEmail(@RequestParam(required = false) String email) {
        HttpStatus status = HttpStatus.OK;
        WrapperDto<BasicEmployeeDetailsDto> wrapperDto = new WrapperDto<>(
                employeeService.findByEmail(email),
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('USER_READ')")
    @GetMapping("/role/{page}")
    @Operation(
            summary = "Gets employee by role",
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
                    )
            }
    )
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

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('USER_READ_SELF')")
    @GetMapping("/me")
    @Operation(
            summary = "Gets the current user's contact information",
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
                            responseCode = "404",
                            description = "Not Found"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    )
            }
    )
    public ResponseEntity<?> getMeById(@AuthenticationPrincipal EmployeeDetails currentUser) {
        HttpStatus status = HttpStatus.OK;
        WrapperDto<BasicEmployeeDetailsDto> wrapperDto = new WrapperDto<>(
                employeeService.findById(currentUser.getId()),
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('USER_READ')")
    @GetMapping("/id/{id}")
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
                            responseCode = "404",
                            description = "Not Found"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
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

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('USER_UPDATE_CONTACT') ")
    @PutMapping("/contact/me")
    @Operation(
            summary = "Updates contact information of employee",
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
                            responseCode = "404",
                            description = "Not Found"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    )
            }
    )
    public ResponseEntity<?> updateMyContact(@RequestBody UpdateEmployeeContactDto updateDto,
                                             @AuthenticationPrincipal EmployeeDetails currentUser) {
        HttpStatus status = HttpStatus.OK;
        WrapperDto<BasicEmployeeDetailsDto> wrapperDto = new WrapperDto<>(
                employeeService.updateEmployeeContact(updateDto, currentUser.getId()),
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('WORKER_UPDATE_NONCONTACT')")
    @PutMapping("/worker/{id}")
    @Operation(
            summary = "Updates non-contact information of employee with the WORKER role",
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
                            responseCode = "404",
                            description = "Not Found"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    )
            }
    )
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

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('MANAGER_UPDATE_NONCONTACT')")
    @PutMapping("/manager/{id}")
    @Operation(
            summary = "Updates non-contact information of employee with the MANAGER role",
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
                            responseCode = "404",
                            description = "Not Found"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    )
            }
    )
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
