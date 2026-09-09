package com.projects.shift_scheduler_api.services;

import com.projects.shift_scheduler_api.dtos.CreateOrUpdateShiftDto;
import com.projects.shift_scheduler_api.dtos.ShiftDateDto;
import com.projects.shift_scheduler_api.dtos.StartAndEndTimeDto;
import com.projects.shift_scheduler_api.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@ExtendWith(MockitoExtension.class)
class InitializeServiceTest {

    public static Employee a1;
    public static Manager m1;
    public static Worker w1, w2;
    public static Shift s1, s2, s3, s4;
    public static CreateOrUpdateShiftDto createDto;
    public static Shift newShift;
    public static StartAndEndTimeDto
            startAndEnd = new StartAndEndTimeDto(),
            endLargerThanStart = new StartAndEndTimeDto();

    @BeforeEach
    void setUpMockData() {
        generateEmployees();
        generateShifts();
        generateDtos();
    }

    void generateEmployees() {
        // employeeId = 1
        a1 = new Employee();
        a1.setUsername("alice_admin");
        a1.setEmail("alice.admin@company.com");
        a1.setPassword("a@123");
        a1.setRole(Role.ADMIN);
        a1.setPosition("System Administrator");
        a1.setDateHired(LocalDate.of(2023, 1, 15));
        a1.setBalance(new BigDecimal("0.00"));

        m1 = new Manager();
        // employeeId = 2
        m1.setUsername("bob_manager");
        m1.setEmail("bob.manager@company.com");
        m1.setPassword("b@123");
        m1.setRole(Role.MANAGER);
        m1.setPosition("Shift Supervisor");
        m1.setDateHired(LocalDate.of(2023, 3, 1));
        m1.setBalance(new BigDecimal("1250.50"));
        m1.setSalary(new BigDecimal("78000.00"));

        w1 = new Worker();
        // employeeId = 3
        w1.setUsername("carol_w");
        w1.setEmail("carol_worker@company.com");
        w1.setPassword("c@123");
        w1.setRole(Role.WORKER);
        w1.setPosition("Store Associate");
        w1.setDateHired(LocalDate.of(2023, 6, 12));
        w1.setBalance(new BigDecimal("450.00"));
        w1.setPayRate(new BigDecimal("22.50"));

        w2 = new Worker();
        // employeeId = 4
        w2.setUsername("david_w");
        w2.setEmail("david_worker@company.com");
        w2.setPassword("d@123");
        w2.setRole(Role.WORKER);
        w2.setPosition("Inventory Associate");
        w2.setDateHired(LocalDate.of(2023, 9, 20));
        w2.setBalance(new BigDecimal("327.75"));
        w2.setPayRate(new BigDecimal("19.00"));

    }

    void generateShifts() {
        s1 = new Shift();
        // shiftId = 1
        s1.setDescription("This is the first shift of May 12, 2026");
        s1.setState(ShiftState.UPCOMING);
        s1.setRequiredEmployeesCount(2);
        s1.setCurrentEmployeeCount(2);
        s1.setStartTime(OffsetDateTime.of(2026, 5, 12, 8,
                0, 0, 0, ZoneOffset.UTC));
        s2 = new Shift();
        // shiftId = 2
        s2.setDescription("This is the first shift of June 20, 2026");
        s2.setState(ShiftState.UPCOMING);
        s2.setRequiredEmployeesCount(3);
        s2.setCurrentEmployeeCount(3);
        s2.setStartTime(OffsetDateTime.of(2026, 6, 20, 8,
                0, 0, 0, ZoneOffset.UTC));

        s3 = new Shift();
        // shiftId = 3
        s3.setDescription("This is the first shift of July 4, 2026");
        s3.setState(ShiftState.UPCOMING);
        s3.setRequiredEmployeesCount(2);
        s3.setCurrentEmployeeCount(1);
        s3.setStartTime(OffsetDateTime.of(2026, 7, 4, 6,
                30, 0, 0, ZoneOffset.UTC));

        s4 = new Shift();
        // shiftId = 4
        s4.setDescription("This is the first shift of August 12, 2026");
        s4.setState(ShiftState.UPCOMING);
        s4.setRequiredEmployeesCount(4);
        s4.setCurrentEmployeeCount(3);
        s4.setStartTime(OffsetDateTime.of(2026, 8, 12, 18,
                0, 0, 0, ZoneOffset.UTC));
    }

    void generateDtos() {
        createDto = new CreateOrUpdateShiftDto();
        createDto.setDescription("This is a shift");
        createDto.setRequiredEmployeeCount(3);
        createDto.setStartTime(new ShiftDateDto(9, 7, 8, 0));
        createDto.setEndTime(new ShiftDateDto(9, 7, 17, 0));
        newShift = mapCreateUpdateDtoToShift(createDto);

        startAndEnd.setStartTime(new ShiftDateDto(9, 7, 8, 0));
        startAndEnd.setEndTime(new ShiftDateDto(9, 7, 17, 0));

        endLargerThanStart.setStartTime(new ShiftDateDto(9, 7, 17, 0));
        endLargerThanStart.setEndTime(new ShiftDateDto(9, 7, 8, 0));
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
