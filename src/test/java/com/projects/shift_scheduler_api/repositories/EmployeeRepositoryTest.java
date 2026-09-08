package com.projects.shift_scheduler_api.repositories;

import com.projects.shift_scheduler_api.models.Employee;
import com.projects.shift_scheduler_api.models.Role;
import com.projects.shift_scheduler_api.models.Worker;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeRepositoryTest extends InitializeRepositoryTest {
    @Test
    void findByUsernameTest() {
        Employee e = employeeRepository.findByUsername(a1.getUsername())
                .orElseThrow(() -> new NoSuchElementException("Does not exist"));
        assertAll(
                () -> assertEquals(a1.getEmail(), e.getEmail()),
                () -> assertThrows(NoSuchElementException.class, () ->
                        employeeRepository.findByUsername("none")
                                .orElseThrow(() -> new NoSuchElementException("Does not exist")))
        );
    }

    @Test
    void findByEmailTest() {
        Employee e = employeeRepository.findByEmail(m1.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Does not exist"));
        assertAll(
                () -> assertEquals(m1.getEmail(), e.getEmail()),
                () -> assertThrows(NoSuchElementException.class, () ->
                        employeeRepository.findByEmail("does@not.exist")
                                .orElseThrow(() -> new NoSuchElementException("Does not exist")))
        );
    }

    @Test
    void findByIdTest() {
        Employee e = employeeRepository.findById(w1.getId()).orElseThrow(() ->
                new NoSuchElementException("Does not exist"));
        assertAll(
                () -> assertEquals(w1.getPayRate(), ((Worker)e).getPayRate()),
                () -> assertThrows(NoSuchElementException.class, () ->
                        employeeRepository.findById(999)
                                .orElseThrow(() -> new NoSuchElementException("Does not exist")))
        );
    }

    @Test
    void existsByUsernameTest() {
        assertAll(
                () -> assertTrue(employeeRepository.existsByUsername(w2.getUsername())),
                () -> assertFalse(employeeRepository.existsByUsername("doesNotExist"))
        );
    }

    @Test
    void existsByEmailTest() {
        assertAll(
                () -> assertTrue(employeeRepository.existsByEmail(w1.getEmail())),
                () -> assertFalse(employeeRepository.existsByEmail("doesNotExist"))
        );
    }

    @Test
    void findByRoleTest() {
        assertAll(
                () -> assertEquals(2, employeeRepository
                        .findByRole(Role.WORKER.name(),
                                PageRequest.of(0, 5)).getTotalElements()),
                () -> assertEquals(0, employeeRepository
                        .findByRole("BYSTANDER", PageRequest.of(0, 5))
                        .getTotalElements())

        );
    }

    @Test
    @Transactional
    void deleteBulkTest() {
        employeeRepository.deleteBulk();
        assertEquals(0, ((List<Employee>)employeeRepository.findAll()).size());
    }
}