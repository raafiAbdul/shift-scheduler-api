package com.projects.shift_scheduler_api.repositories;

import com.projects.shift_scheduler_api.models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InitializeRepositoryTest {

    public static Employee a1;
    public static Manager m1;
    public static Worker w1, w2;
    public static Shift s1, s2, s3, s4;

    @Autowired public EmployeeRepository employeeRepository;
    @Autowired public ShiftRepository shiftRepository;
    @Autowired public EmployeeShiftRepository employeeShiftRepository;

    @BeforeAll
    void setUpMockData() {
        generateEmployees();
        generateShifts();
    }

    void generateEmployees() {
        // employeeId = 1
        a1 = new Employee();
        a1.setUsername("alice_admin");
        a1.setEmail("alice.admin@company.com");
        a1.setPassword("a@123ALICE");
        a1.setRole(Role.ADMIN);
        a1.setPosition("System Administrator");
        a1.setDateHired(LocalDate.of(2023, 1, 15));
        a1.setBalance(new BigDecimal("0.00"));

        m1 = new Manager();
        // employeeId = 2
        m1.setUsername("bob_manager");
        m1.setEmail("bob.manager@company.com");
        m1.setPassword("b!123BOB");
        m1.setRole(Role.MANAGER);
        m1.setPosition("Shift Supervisor");
        m1.setDateHired(LocalDate.of(2023, 3, 1));
        m1.setBalance(new BigDecimal("1250.50"));
        m1.setSalary(new BigDecimal("78000.00"));

        w1 = new Worker();
        // employeeId = 3
        w1.setUsername("carol_w");
        w1.setEmail("carol_worker@company.com");
        w1.setPassword("c!123CAROL");
        w1.setRole(Role.WORKER);
        w1.setPosition("Store Associate");
        w1.setDateHired(LocalDate.of(2023, 6, 12));
        w1.setBalance(new BigDecimal("450.00"));
        w1.setPayRate(new BigDecimal("22.50"));

        w2 = new Worker();
        // employeeId = 4
        w2.setUsername("david_w");
        w2.setEmail("david_worker@company.com");
        w2.setPassword("d!123DAVID");
        w2.setRole(Role.WORKER);
        w2.setPosition("Inventory Associate");
        w2.setDateHired(LocalDate.of(2023, 9, 20));
        w2.setBalance(new BigDecimal("327.75"));
        w2.setPayRate(new BigDecimal("19.00"));

        employeeRepository.saveAll(List.of(a1, m1, w1, w2));

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
        s1.setEndTime(OffsetDateTime.of(2026, 5, 12, 17,
                0, 0, 0, ZoneOffset.UTC));

        s2 = new Shift();
        // shiftId = 2
        s2.setDescription("This is the first shift of June 20, 2026");
        s2.setState(ShiftState.UPCOMING);
        s2.setRequiredEmployeesCount(3);
        s2.setCurrentEmployeeCount(3);
        s2.setStartTime(OffsetDateTime.of(2026, 6, 20, 8,
                0, 0, 0, ZoneOffset.UTC));
        s2.setEndTime(OffsetDateTime.of(2026, 6, 20, 17,
                0, 0, 0, ZoneOffset.UTC));

        s3 = new Shift();
        // shiftId = 3
        s3.setDescription("This is the first shift of July 4, 2026");
        s3.setState(ShiftState.UPCOMING);
        s3.setRequiredEmployeesCount(2);
        s3.setCurrentEmployeeCount(1);
        s3.setStartTime(OffsetDateTime.of(2026, 7, 4, 6,
                30, 0, 0, ZoneOffset.UTC));
        s3.setEndTime(OffsetDateTime.of(2026, 7, 4, 15,
                30, 0, 0, ZoneOffset.UTC));

        s4 = new Shift();
        // shiftId = 4
        s4.setDescription("This is the first shift of August 12, 2026");
        s4.setState(ShiftState.UPCOMING);
        s4.setRequiredEmployeesCount(4);
        s4.setCurrentEmployeeCount(3);
        s4.setStartTime(OffsetDateTime.of(2026, 8, 12, 18,
                0, 0, 0, ZoneOffset.UTC));
        s4.setEndTime(OffsetDateTime.of(2026, 8, 13, 7,
                0, 0, 0, ZoneOffset.UTC));

        List.of(m1, w1).forEach(e -> s1.addEmployee(e));
        List.of(m1, w1, w2).forEach(e -> s2.addEmployee(e));
        s3.addEmployee(w2);
        List.of(m1, w1, w2).forEach(e -> s4.addEmployee(e));

        shiftRepository.saveAll(List.of(s1, s2, s3, s4));
    }

}
