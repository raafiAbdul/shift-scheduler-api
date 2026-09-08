package com.projects.shift_scheduler_api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @NotNull
    @Size(min = 3, max = 30)
    private String username;

    @Email(
            regexp = "[\\w\\.-]{2,30}@[\\w\\.-]{2,20}\\.[a-z[A-Z]]{2,10}",
            message = "Please enter a valid email such as user@domain.com"
    )
    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    @Size(min = 8, max = 100)
    @Pattern(regexp = "(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*\\p{P})[\\w[\\p{P}]]{8,}",
            message = "Should contain punctuation, a number, lower and upper case letters.")
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

    private String position;

    @Column(name = "date_hired")
    private LocalDate dateHired;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmployeeShift> employeeShifts = new ArrayList<>();

    @PositiveOrZero
    @NotNull
    private BigDecimal balance;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public List<EmployeeShift> getEmployeeShifts() {
        return employeeShifts;
    }

    public void setEmployeeShifts(List<EmployeeShift> employeeShifts) {
        this.employeeShifts = employeeShifts;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDateHired() {
        return dateHired;
    }

    public void setDateHired(LocalDate dateHired) {
        this.dateHired = dateHired;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Employee employee)) return false;
        return id == employee.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", dateHired=" + dateHired +
                '}';
    }

}
