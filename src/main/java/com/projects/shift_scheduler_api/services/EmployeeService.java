package com.projects.shift_scheduler_api.services;

import com.projects.shift_scheduler_api.dtos.BasicEmployeeDetailsDto;
import com.projects.shift_scheduler_api.dtos.CreateOrUpdateShiftDto;
import com.projects.shift_scheduler_api.dtos.RegisterDto;
import com.projects.shift_scheduler_api.models.Employee;
import com.projects.shift_scheduler_api.models.Manager;
import com.projects.shift_scheduler_api.models.Role;
import com.projects.shift_scheduler_api.models.Worker;
import com.projects.shift_scheduler_api.repositories.EmployeeRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public Employee createEmployee(@Valid Employee employee) {
        return employeeRepository.save(employee);
    }

    @Transactional
    public Manager createManager(@Valid Manager manager) {
        return employeeRepository.save(manager);
    }

    @Transactional
    public Worker createWorker(@Valid Worker worker) {
        return employeeRepository.save(worker);
    }

    @Transactional
    public Worker updateWorker(long id, @Valid Worker employee) {
        Employee oldEmployee = employeeRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("No such worker with id #" + id));
        if(!(oldEmployee.getRole() == Role.WORKER)) {
            throw new IllegalArgumentException("Expected an employee with MANAGER role");
        }
        Worker old = (Worker) oldEmployee;
        old.setUsername(employee.getUsername());
        old.setEmail(employee.getEmail());
        old.setPassword(employee.getPassword());
        old.setRole(employee.getRole());
        old.setPosition(employee.getPosition());
        old.setDateHired(employee.getDateHired());
        old.setBalance(employee.getBalance());
        old.setPayRate(employee.getPayRate());

        return employeeRepository.save(old);
    }

    @Transactional
    public Manager updateManager(long id, @Valid Manager employee) {
        Employee oldEmployee = employeeRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("No such worker with id #" + id));
        if(!(oldEmployee.getRole() == Role.MANAGER)) {
            throw new IllegalArgumentException("Expected an employee with MANAGER role");
        }
        Manager old = (Manager) oldEmployee;
        old.setUsername(employee.getUsername());
        old.setEmail(employee.getEmail());
        old.setPassword(employee.getPassword());
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
        return mapEmployeeToBasicEmployee(employeeRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("No such employee with username " + username)));
    }

    public BasicEmployeeDetailsDto findByEmail(String email) {
        if(email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Null or blank email");
        } else if(!employeeRepository.existsByEmail(email)) {
            throw new NoSuchElementException("No such email exists");
        }
        return mapEmployeeToBasicEmployee(employeeRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("No such employee with email " + email)));
    }

    public Page<BasicEmployeeDetailsDto> findByRole(String role, Integer size, Integer page) {
        size = (size == null || size <= 0) ? 10 : size;
        page = (page == null || page < 0) ? 0 : page;

        if(role == null || role.isEmpty()) {
            throw new IllegalArgumentException("Null or empty role argument");
        }

        Page<Employee> employeePage = employeeRepository.findByRole(
                role.toUpperCase(), PageRequest.of(page, size));

        return employeePage.map(this::mapEmployeeToBasicEmployee);
    }

    public Page<BasicEmployeeDetailsDto> findBasicDetails(Integer size, Integer page) {
        size = (size == null || size <= 0) ? 10 : size;
        page = (page == null || page < 0) ? 0 : page;

        Page<Employee> employeePage = employeeRepository.findAll(PageRequest.of(page, size));
        return employeePage.map(this::mapEmployeeToBasicEmployee);
    }

    public BasicEmployeeDetailsDto findById(Long id) {
        if(id == null || id < 0)
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

        employee.setUsername(registerDto.getUsername());
        employee.setEmail(registerDto.getEmail());
        employee.setPassword(registerDto.getPassword());
        employee.setRole(registerDto.getRole());

        return mapEmployeeToBasicEmployee(employeeRepository.save(employee));
    }

    private BasicEmployeeDetailsDto mapEmployeeToBasicEmployee(Employee e) {
        return new BasicEmployeeDetailsDto(
                e.getUsername(),
                e.getPosition(),
                e.getRole(),
                e.getDateHired()
        );
    }

}
