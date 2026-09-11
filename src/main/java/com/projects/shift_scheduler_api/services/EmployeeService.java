package com.projects.shift_scheduler_api.services;

import com.projects.shift_scheduler_api.dtos.*;
import com.projects.shift_scheduler_api.models.Employee;
import com.projects.shift_scheduler_api.models.Manager;
import com.projects.shift_scheduler_api.models.Role;
import com.projects.shift_scheduler_api.models.Worker;
import com.projects.shift_scheduler_api.repositories.EmployeeRepository;
import com.projects.shift_scheduler_api.security.services.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@Service
public class EmployeeService {

    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final PasswordEncoder encoder;
    private final EmployeeRepository employeeRepository;

    public EmployeeService(JwtService jwtService, AuthenticationManager authManager, PasswordEncoder encoder, EmployeeRepository employeeRepository) {
        this.jwtService = jwtService;
        this.authManager = authManager;
        this.encoder = encoder;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public Manager createManager(@Valid Manager manager) {
        if(manager.getRole() == Role.ADMIN)
            throw new IllegalArgumentException("Can't create another admin");

        String encodedPassword = encoder.encode(manager.getPassword());
        manager.setPassword(encodedPassword);

        return employeeRepository.save(manager);
    }

    @Transactional
    public Worker createWorker(@Valid Worker worker) {
        if(worker.getRole() == Role.ADMIN)
            throw new IllegalArgumentException("Can't create another admin");

        String encodedPassword = encoder.encode(worker.getPassword());
        worker.setPassword(encodedPassword);

        return employeeRepository.save(worker);
    }

    @Transactional
    public Worker updateWorker(Long id, @Valid Worker employee) {
        Employee oldEmployee = employeeRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("No such worker with id #" + id));
        if(!(oldEmployee.getRole() == Role.WORKER)) {
            throw new IllegalArgumentException("Expected an employee with MANAGER role");
        }

        String encodedPassword = encoder.encode(employee.getPassword());

        Worker old = (Worker) oldEmployee;
        old.setUsername(employee.getUsername());
        old.setEmail(employee.getEmail());
        old.setPassword(encodedPassword);
        old.setRole(employee.getRole());
        old.setPosition(employee.getPosition());
        old.setDateHired(employee.getDateHired());
        old.setBalance(employee.getBalance());
        old.setPayRate(employee.getPayRate());

        return employeeRepository.save(old);
    }

    @Transactional
    public Manager updateManager(Long id, @Valid Manager employee) {
        Employee oldEmployee = employeeRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("No such worker with id #" + id));
        if(!(oldEmployee.getRole() == Role.MANAGER)) {
            throw new IllegalArgumentException("Expected an employee with MANAGER role");
        }

        String encodedPassword = encoder.encode(employee.getPassword());

        Manager old = (Manager) oldEmployee;
        old.setUsername(employee.getUsername());
        old.setEmail(employee.getEmail());
        old.setPassword(encodedPassword);
        old.setRole(employee.getRole());
        old.setPosition(employee.getPosition());
        old.setDateHired(employee.getDateHired()); // expected format yyyy-MM-dd
        old.setBalance(employee.getBalance());
        old.setSalary(employee.getSalary());

        return employeeRepository.save(old);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        if(id == null || id < 0) {
            throw new IllegalArgumentException("ID out of bounds");
        } else if(!employeeRepository.existsById(id)) {
            throw new NoSuchElementException("No such employee exists");
        }

        employeeRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        employeeRepository.deleteBulk();
    }

    public Page<Employee> findAll(Integer page, Integer size) {
        page = (page == null || page < 0) ? 0 : page;
        size = (size == null || size <= 0) ? 10 : size;
        return employeeRepository.findAll(PageRequest.of(page, size));
    }

    // =========================== FOR THE EVERYONE ELSE ===========================

    public Page<BasicEmployeeDetailsDto> findAllBasic(Integer page, Integer size) {
        page = (page == null || page < 0) ? 0 : page;
        size = (size == null || size <= 0) ? 10 : size;
        Page<Employee> employeePage = employeeRepository.findAll(PageRequest.of(page, size));
        return employeePage.map(this::mapEmployeeToBasicEmployee);
    }

    public BasicEmployeeDetailsDto findByUsername(String username) {
        if(username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Null or blank username");
        } else if(!employeeRepository.existsByUsername(username)) {
            throw new NoSuchElementException("No such username exists");
        }
        return mapEmployeeToBasicEmployee(employeeRepository.findByUsername(username.trim())
                .orElseThrow(() -> new NoSuchElementException("No such employee with username " + username)));
    }

    public BasicEmployeeDetailsDto findByEmail(String email) {
        if(email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Null or blank email");
        } else if(!employeeRepository.existsByEmail(email.trim())) {
            throw new NoSuchElementException("No such email exists");
        }
        return mapEmployeeToBasicEmployee(employeeRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("No such employee with email " + email)));
    }

    public Page<BasicEmployeeDetailsDto> findByRole(String role, Integer size, Integer page) {
        size = (size == null || size <= 0) ? 10 : size;
        page = (page == null || page < 0) ? 0 : page;

        if(role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Null or empty role argument");
        }
        Page<Employee> employeePage = employeeRepository.findByRole(
                role.trim().toUpperCase(), PageRequest.of(page, size));

        return employeePage.map(this::mapEmployeeToBasicEmployee);
    }

    public BasicEmployeeDetailsDto findById(Long id) {
        if(id == null || id <= 0)
            throw new IllegalArgumentException("Null/negative ID");

        Employee employee = employeeRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("No such employee"));

        return mapEmployeeToBasicEmployee(employee);

    }

    @Transactional
    public BasicEmployeeDetailsDto registerEmployee(@Valid RegisterDto registerDto) {
        Employee employee;
        if(registerDto.getRole() == Role.MANAGER) {
            employee = new Manager();
        } else if(registerDto.getRole() == Role.WORKER) {
            employee = new Worker();
        } else {
            throw new NoSuchElementException("No such role");
        }

        String encodedPassword = encoder.encode(registerDto.getPassword());
        registerDto.setPassword(encodedPassword);

        employee.setUsername(registerDto.getUsername());
        employee.setEmail(registerDto.getEmail());
        employee.setPassword(registerDto.getPassword());
        employee.setRole(registerDto.getRole());
        employee.setDateHired(LocalDate.now());

        return mapEmployeeToBasicEmployee(employeeRepository.save(employee));
    }

    @Transactional
    public BasicEmployeeDetailsDto updateEmployeeContact(@Valid UpdateEmployeeContactDto contactDto, Long id) {

        if(id == null || id <= 0)
            throw new IllegalArgumentException("Null/negative ID");

        Employee e = employeeRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("No such employee with id #" + id));

        e.setUsername(contactDto.getUsername());
        e.setEmail(contactDto.getEmail());

        return mapEmployeeToBasicEmployee(employeeRepository.save(e));
    }

    @Transactional
    public BasicEmployeeDetailsDto updateWorkerNonContact(@Valid UpdateWorkerNonContactDto contactDto, Long id) {
        if(id == null || id <= 0)
            throw new IllegalArgumentException("Null/negative ID");

        Worker w = (Worker) employeeRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("No such employee with id #" + id));

        w.setPayRate(contactDto.getPayRate());
        w.setPosition(contactDto.getPosition());
        w.setBalance(contactDto.getBalance());
        w.setRole(contactDto.getRole());

        return mapEmployeeToBasicEmployee(employeeRepository.save((Employee) w));
    }

    @Transactional
    public BasicEmployeeDetailsDto updateManagerNonContact(@Valid UpdateManagerNonContactDto contactDto, Long id) {
        if(id == null || id <= 0)
            throw new IllegalArgumentException("Null/negative ID");

        Manager m = (Manager) employeeRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("No such employee with id #" + id));

        m.setSalary(contactDto.getSalary());
        m.setPosition(contactDto.getPosition());
        m.setBalance(contactDto.getBalance());
        m.setRole(contactDto.getRole());

        return mapEmployeeToBasicEmployee(employeeRepository.save((Employee) m));
    }

    public String verify(@Valid LoginDto loginDto) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(), loginDto.getPassword()));

        if(!authentication.isAuthenticated())
            throw new BadCredentialsException("Invalid credentials");

        return jwtService.generateToken(loginDto.getUsername());
    }

    private BasicEmployeeDetailsDto mapEmployeeToBasicEmployee(Employee e) {
        return new BasicEmployeeDetailsDto(e);
    }

}
