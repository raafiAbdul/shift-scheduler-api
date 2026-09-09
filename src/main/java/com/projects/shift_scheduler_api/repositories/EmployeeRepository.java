package com.projects.shift_scheduler_api.repositories;

import com.projects.shift_scheduler_api.models.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends CrudRepository<Employee, Long>,
        PagingAndSortingRepository<Employee, Long> {
    Optional<Employee> findByUsername(@Param(value = "username") String username);
    Optional<Employee> findByEmail(@Param(value = "email")String email);

    Optional<Employee> findById(long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query("select e from Employee e where lower(e.role) like lower(:role)")
    Page<Employee> findByRole(String role, Pageable pageable);

    @Query(value = "delete from employee_shift; delete from employees", nativeQuery = true)
    @Modifying
    void deleteBulk();

}
