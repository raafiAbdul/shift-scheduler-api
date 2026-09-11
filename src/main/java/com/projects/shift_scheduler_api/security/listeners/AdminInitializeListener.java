package com.projects.shift_scheduler_api.security.listeners;

import com.projects.shift_scheduler_api.models.Employee;
import com.projects.shift_scheduler_api.models.Role;
import com.projects.shift_scheduler_api.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
public class AdminInitializeListener {

    @Value("${admin.password}")
    private String password;

    @Value("${admin.username}")
    private String username;

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder encoder;

    public AdminInitializeListener(EmployeeRepository employeeRepository, PasswordEncoder encoder) {
        this.employeeRepository = employeeRepository;
        this.encoder = encoder;
    }

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void adminInit() {
        if(employeeRepository.findByUsername(username).isEmpty()) {
            Employee e = new Employee();
            e.setUsername(username);
            e.setPassword(encoder.encode(password));
            e.setRole(Role.ADMIN);
            e.setEmail("admin@role.com");
            e.setDateHired(LocalDate.now());
            employeeRepository.save(e);
        }
    }

}
