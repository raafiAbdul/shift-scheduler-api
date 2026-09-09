package com.projects.shift_scheduler_api.security.services;

import com.projects.shift_scheduler_api.models.Employee;
import com.projects.shift_scheduler_api.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityEmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public Employee register(Employee employee) {
        String encodedPassword = encoder.encode(employee.getPassword());
        employee.setPassword(encodedPassword);
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return (List<Employee>) employeeRepository.findAll();
    }

    public String verify(Employee employee) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        employee.getUsername(), employee.getPassword()));

        if(authentication.isAuthenticated()) {
            return jwtService.generateToken(employee.getUsername());
        } else {
            return "Authentication failed";
        }

    }
}
