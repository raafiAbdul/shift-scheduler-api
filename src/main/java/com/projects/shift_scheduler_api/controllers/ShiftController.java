package com.projects.shift_scheduler_api.controllers;

import com.projects.shift_scheduler_api.dtos.CreateOrUpdateShiftDto;
import com.projects.shift_scheduler_api.dtos.ShiftDto;
import com.projects.shift_scheduler_api.dtos.StartAndEndTimeDto;
import com.projects.shift_scheduler_api.dtos.WrapperDto;
import com.projects.shift_scheduler_api.security.models.EmployeeDetails;
import com.projects.shift_scheduler_api.services.EmployeeShiftService;
import com.projects.shift_scheduler_api.services.ShiftService;
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

@RestController
@RequestMapping("/api/v1/shift")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Shift Related Endpoints")
public class ShiftController {

    private final ShiftService shiftService;
    private final EmployeeShiftService employeeShiftService;

    public ShiftController(ShiftService shiftService, EmployeeShiftService employeeShiftService) {
        this.shiftService = shiftService;
        this.employeeShiftService = employeeShiftService;
    }

    @PreAuthorize("hasAuthority('SHIFT_CLAIM')")
    @PostMapping("/add-employee")
    @Operation(
            summary = "Adds current employee to the shift using the shift's ID",
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
    public ResponseEntity<?> addEmployee(@RequestParam Long shiftId,
                                         @AuthenticationPrincipal EmployeeDetails currentUser) {
        HttpStatus status = HttpStatus.OK;
        WrapperDto<ShiftDto> wrapperDto = new WrapperDto<>(
                shiftService.addEmployeeToShift(currentUser.getId(), shiftId),
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }

    @PreAuthorize("hasAuthority('SHIFT_DROP')")
    @DeleteMapping("/remove-employee")
    @Operation(
            summary = "Removes current employee from the shift using the shift's ID",
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
                            responseCode = "404",
                            description = "Not Found"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    )
            }
    )
    public ResponseEntity<?> removeEmployee(@RequestParam Long shiftId,
                                            @AuthenticationPrincipal EmployeeDetails currentUser) {
        shiftService.removeEmployeeFromShift(currentUser.getId(), shiftId);
        HttpStatus status = HttpStatus.NO_CONTENT;
        WrapperDto<Void> wrapperDto = new WrapperDto<>(
                null,
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }

    @PreAuthorize("hasAuthority('SHIFT_WRITE')")
    @PostMapping
    @Operation(
            summary = "Creates a new shift",
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
    public ResponseEntity<?> create(@RequestBody @Valid CreateOrUpdateShiftDto createDto) {
        HttpStatus status = HttpStatus.CREATED;
        WrapperDto<ShiftDto> wrapperDto = new WrapperDto<>(
                shiftService.createShift(createDto),
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }

    @PreAuthorize("hasAuthority('SHIFT_UPDATE')")
    @PutMapping
    @Operation(
            summary = "Updates a new shift",
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
    public ResponseEntity<?> update(@RequestParam Long id,
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
    @PreAuthorize("hasAuthority('SHIFT_DELETE')")
    @Operation(
            summary = "Deletes shift by ID",
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
    @PreAuthorize("hasAuthority('SHIFT_READ')")
    @Operation(
            summary = "Gets shift by ID",
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
        WrapperDto<ShiftDto> wrapperDto = new WrapperDto<>(
                shiftService.findById(id),
                status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }

    @GetMapping("/description/{page}")
    @PreAuthorize("hasAuthority('SHIFT_READ')")
    @Operation(
            summary = "Gets shifts by the matching description",
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
    @PreAuthorize("hasRole('MANAGER', 'ADMIN')")
    @Operation(
            summary = "Gets shifts with a certain employee's username tied to it",
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
    @PreAuthorize("hasAuthority('SHIFT_READ')")
    @Operation(
            summary = "Gets shifts who's times overlap, in anyway, with the provided start and end times",
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
    @PreAuthorize("hasAuthority('CLOCK_IN')")
    @Operation(
            summary = "Lets current employee clock in to a shift using the shift ID",
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
    public ResponseEntity<?> clockIn(@RequestParam Long shiftId,
                                     @AuthenticationPrincipal EmployeeDetails currentUser) {
        employeeShiftService.clockIn(shiftId, currentUser.getId());
        HttpStatus status = HttpStatus.OK;
        WrapperDto<Void> wrapperDto = new WrapperDto<>(
                null, status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }

    @PutMapping("/clock-out")
    @PreAuthorize("hasAuthority('CLOCK_OUT')")
    @Operation(
            summary = "Lets current employee clock out from a shift using the shift ID",
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
    public ResponseEntity<?> clockOut(@RequestParam Long shiftId,
                                      @AuthenticationPrincipal EmployeeDetails currentUser) {
        employeeShiftService.clockOut(shiftId, currentUser.getId());
        HttpStatus status = HttpStatus.OK;
        WrapperDto<Void> wrapperDto = new WrapperDto<>(
                null, status.value(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(wrapperDto);
    }
}
