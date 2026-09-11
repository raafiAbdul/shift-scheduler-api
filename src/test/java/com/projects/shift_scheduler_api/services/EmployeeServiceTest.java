package com.projects.shift_scheduler_api.services;

import com.projects.shift_scheduler_api.dtos.*;
import com.projects.shift_scheduler_api.models.Employee;
import com.projects.shift_scheduler_api.models.Manager;
import com.projects.shift_scheduler_api.models.Role;
import com.projects.shift_scheduler_api.models.Worker;
import com.projects.shift_scheduler_api.repositories.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest extends InitializeServiceTest {
    @Mock
    EmployeeRepository employeeRepository;
    @Mock
    PasswordEncoder encoder;
    @InjectMocks
    EmployeeService employeeService;

    @Test
    void shouldReturn_TheSame_ManagerUsernameAndEmail() {
        when(employeeRepository.save(m1)).thenReturn(m1);
        when(encoder.encode(m1.getPassword())).thenReturn("");
        Manager m2 = employeeService.createManager(m1);
        assertAll(
                () -> assertEquals("bob_manager", m2.getUsername()),
                () -> assertEquals("bob.manager@company.com", m2.getEmail()),
                () -> assertThrows(IllegalArgumentException.class, () -> {
                    m1.setRole(Role.ADMIN);
                    employeeService.createManager(m1);
                })
        );
        m1.setRole(Role.MANAGER);
    }

    @Test
    void shouldReturn_TheSame_WorkerUsernameAndEmail() {
        when(employeeRepository.save(w1)).thenReturn(w1);
        when(encoder.encode(w1.getPassword())).thenReturn("asdfasdg");
        Worker w1_2 = employeeService.createWorker(w1);
        assertAll(
                () -> assertEquals("carol_w", w1_2.getUsername()),
                () -> assertEquals("carol_worker@company.com", w1_2.getEmail()),
                () -> assertThrows(IllegalArgumentException.class, () -> {
                    w1.setRole(Role.ADMIN);
                    employeeService.createWorker(w1);
                })
        );
        w1.setRole(Role.MANAGER);
    }

    @Test
    void verifyFindAll_InputsNullOrAcceptedInput_forFindAll() {
        employeeService.findAll(null, null);
        verify(employeeRepository, times(1))
                .findAll(PageRequest.of(0, 10));
        employeeService.findAll(2, 11);
        verify(employeeRepository, times(1))
                .findAll(PageRequest.of(2, 11));
    }

    @Test
    void updateWorker_ShouldThrow_IllegalArgumentException() {
        Long id = 2L;
        when(employeeRepository.findById(id))
                .thenReturn(Optional.of(m1));
        assertThrows(IllegalArgumentException.class, () -> employeeService.updateWorker(id, new Worker()));
    }

    @Test
    void updateManager_ShouldThrow_IllegalArgumentException() {
        Long id = 2L;
        when(employeeRepository.findById(id))
                .thenReturn(Optional.of(w2));
        assertThrows(IllegalArgumentException.class, () -> employeeService.updateManager(id, new Manager()));
    }

    @Test
    void updateManager_happyFlow() {
        Long id = 2L;
        when(employeeRepository.findById(id))
                .thenReturn(Optional.of(m1));
        Manager m2 = m1;
        m2.setEmail("cool.cars@store.com");
        when(encoder.encode(m2.getPassword())).thenReturn("asteaf");
        when(employeeRepository.save(any(Manager.class))).thenReturn(m2);
        Employee m3 = employeeService.updateManager(id, m2);
        assertEquals("cool.cars@store.com", m3.getEmail());
    }

    @Test
    void updateWorker_happyFlow() {
        Long id = 2L;
        when(employeeRepository.findById(id))
                .thenReturn(Optional.of(w1));
        when(employeeRepository.save(any(Worker.class))).thenReturn(w1);
        when(encoder.encode(w1.getPassword())).thenReturn("");
        Worker w1_2 = employeeService.updateWorker(id, w1);
        verify(employeeRepository, times(1)).save(w1);
        assertEquals("carol_worker@company.com", w1_2.getEmail());
    }

    @Test
    void deleteEmployee_errorFlows() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () ->
                    employeeService.deleteEmployee(-3L)),
                () -> assertThrows(IllegalArgumentException.class, () ->
                    employeeService.deleteEmployee(null)),
                () -> assertThrows(NoSuchElementException.class, () -> {
                    when(employeeRepository.existsById(any(Long.class))).thenReturn(false);
                    employeeService.deleteEmployee(3L);
                })
        );
    }

    @Test
    void deleteEmployee_shouldInvoke_deleteById() {
        when(employeeRepository.existsById(4L)).thenReturn(true);
        employeeService.deleteEmployee(4L);
        verify(employeeRepository, times(1)).deleteById(4L);
    }

    @Test
    void deleteAll_happyFlow() {
        employeeService.deleteAll();
        verify(employeeRepository, times(1)).deleteBulk();
    }

    @Test
    void findByUsername_errorFlows() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () ->
                    employeeService.findByUsername("")),
                () -> assertThrows(IllegalArgumentException.class, () ->
                    employeeService.findByUsername(null)),
                () -> assertThrows(NoSuchElementException.class, () -> {
                    when(employeeRepository.existsByUsername(any(String.class))).thenReturn(false);
                    employeeService.findByUsername("yellow_pad_paper");
                })
        );
    }

    @Test
    void findByEmail_errorFlows() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () ->
                    employeeService.findByEmail("")),
                () -> assertThrows(IllegalArgumentException.class, () ->
                    employeeService.findByEmail(null)),
                () -> assertThrows(NoSuchElementException.class, () ->
                    employeeService.findByUsername("nobody@knows.com"))
        );
    }

    @Test
    void findByUsername_happyFlow() {
        when(employeeRepository.existsByUsername(any(String.class))).thenReturn(true);
        when(employeeRepository.findByUsername(any(String.class))).thenReturn(Optional.of(w1));
        employeeService.findByUsername(w1.getUsername());
        verify(employeeRepository, times(1)).findByUsername(w1.getUsername());
    }

    @Test
    void findByEmail_happyFlow() {
        when(employeeRepository.existsByEmail(any(String.class))).thenReturn(true);
        when(employeeRepository.findByEmail(any(String.class))).thenReturn(Optional.of(w1));
        employeeService.findByEmail(w1.getEmail());
        verify(employeeRepository, times(1)).findByEmail(w1.getEmail());
    }

    @Test
    void verify_FindByRole_givenPageDetails() {
        when(employeeRepository.findByRole("MANAGER", PageRequest.of(2, 11)))
                .thenReturn(new PageImpl<>(List.of(new Employee())));
        employeeService.findByRole("manager", 11, 2);
        verify(employeeRepository, times(1))
                .findByRole("manager".toUpperCase(), PageRequest.of(2, 11));
    }

    @Test
    void verify_FindByRole_givenNoPageDetails() {
        when(employeeRepository.findByRole("WORKER", PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(new Employee())));
        employeeService.findByRole("worker", null, null);
        verify(employeeRepository, times(1))
                .findByRole("worker".toUpperCase(), PageRequest.of(0, 10));
    }

    @Test
    void findByRole_errorFlows() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () ->
                    employeeService.findByRole(null, null, null)),
                () -> assertThrows(IllegalArgumentException.class, () ->
                    employeeService.findByRole("", null, null))
        );
    }

    @Test
    void registerEmployee_resultsInManagerType() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setRole(Role.MANAGER);
        registerDto.setPassword("");
        when(encoder.encode(registerDto.getPassword())).thenReturn("");
        when(employeeRepository.save(any(Employee.class))).thenReturn(new Manager());
        assertInstanceOf(BasicEmployeeDetailsDto.class, employeeService.registerEmployee(registerDto));
    }

    @Test
    void registerEmployee_resultsInWorkerType() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setRole(Role.WORKER);
        registerDto.setPassword("");

        when(employeeRepository.save(any(Employee.class))).thenReturn(new Worker());
        when(encoder.encode(registerDto.getPassword())).thenReturn("asdga");
        assertInstanceOf(BasicEmployeeDetailsDto.class, employeeService.registerEmployee(registerDto));
    }

    @Test
    void registerEmployee_resultsInNoSuchElementException() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setRole(Role.ADMIN);
        assertThrows(NoSuchElementException.class, () -> employeeService.registerEmployee(registerDto));
    }

    @Test
    void findByUser_validatesSizeAndPage() {
        Integer page = -2;
        when(employeeRepository.findAll(PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(new Employee())));
        employeeService.findAllBasic(page, null);
        verify(employeeRepository, times(1))
                .findAll(PageRequest.of(0, 10));
    }

    @Test
    void findByUser_happyFlow() {
        Integer size = 14, page = 3;
        when(employeeRepository.findAll(PageRequest.of(3, 14)))
                .thenReturn(new PageImpl<>(List.of(new Employee())));
        employeeService.findAllBasic(page, size);
        verify(employeeRepository, times(1))
                .findAll(PageRequest.of(3, 14));
    }

    @Test
    void findById_happyFlow() {
        Long id = 1L;
        when(employeeRepository.findById(id)).thenReturn(Optional.of(w2));

        employeeService.findById(id);

        verify(employeeRepository, times(1)).findById(id);
    }

    @Test
    void findById_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                employeeService.findById(-3L));
    }

    @Test
    void updateEmployeeContact_happyFlow() {
        Long id = 2L;
        when(employeeRepository.findById(id)).thenReturn(Optional.of(a1));
        when(employeeRepository.save(a1)).thenReturn(a1);
        UpdateEmployeeContactDto contactDto = new UpdateEmployeeContactDto();

        contactDto.setUsername(a1.getUsername());
        contactDto.setEmail(a1.getEmail());

        employeeService.updateEmployeeContact(contactDto, id);

        verify(employeeRepository, times(1)).save(a1);
    }

    @Test
    void updateEmployeeContact_errorFlows() {
        UpdateEmployeeContactDto contactDto = new UpdateEmployeeContactDto();

        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () ->
                        employeeService.updateEmployeeContact(contactDto, null)),
                () -> assertThrows(NoSuchElementException.class, () -> {
                    when(employeeRepository.findById(any(Long.class))).thenReturn(Optional.empty());
                    employeeService.updateEmployeeContact(contactDto, 2L);
                })
        );
    }

    @Test
    void updateWorkerNonContact_happyFlow() {
        UpdateWorkerNonContactDto contactDto = new UpdateWorkerNonContactDto();
        Long id = 1L;
        when(employeeRepository.findById(id)).thenReturn(Optional.of(w1));
        when(employeeRepository.save(w1)).thenReturn(w1);

        employeeService.updateWorkerNonContact(contactDto, id);

        verify(employeeRepository, times(1)).findById(id);
        verify(employeeRepository, times(1)).save(w1);


    }

    @Test
    void updateWorkerNonContact_errorFlows() {
        UpdateWorkerNonContactDto contactDto = new UpdateWorkerNonContactDto();

        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () ->
                        employeeService.updateWorkerNonContact(contactDto, null)),
                () -> assertThrows(NoSuchElementException.class, () -> {
                    when(employeeRepository.findById(any(Long.class))).thenReturn(Optional.empty());
                    employeeService.updateWorkerNonContact(contactDto, 2L);
                })
        );
    }

    @Test
    void updateManagerNonContact_happyFlow() {
        UpdateManagerNonContactDto contactDto = new UpdateManagerNonContactDto();
        Long id = 1L;
        when(employeeRepository.findById(id)).thenReturn(Optional.of(m1));
        when(employeeRepository.save(m1)).thenReturn(m1);

        employeeService.updateManagerNonContact(contactDto, id);

        verify(employeeRepository, times(1)).findById(id);
        verify(employeeRepository, times(1)).save(w1);


    }

    @Test
    void updateManagerNonContact_errorFlows() {
        UpdateManagerNonContactDto contactDto = new UpdateManagerNonContactDto();

        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () ->
                        employeeService.updateManagerNonContact(contactDto, null)),
                () -> assertThrows(NoSuchElementException.class, () -> {
                    when(employeeRepository.findById(any(Long.class))).thenReturn(Optional.empty());
                    employeeService.updateManagerNonContact(contactDto, 2L);
                })
        );
    }



}