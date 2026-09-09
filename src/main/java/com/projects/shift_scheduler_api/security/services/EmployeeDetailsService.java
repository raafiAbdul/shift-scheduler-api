package com.projects.shift_scheduler_api.security.services;

import com.projects.shift_scheduler_api.models.Employee;
import com.projects.shift_scheduler_api.repositories.EmployeeRepository;
import com.projects.shift_scheduler_api.security.models.EmployeeDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EmployeeDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    public EmployeeDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Employee employee =  employeeRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Employee not found"));

        return new EmployeeDetails(employee);
    }
}
