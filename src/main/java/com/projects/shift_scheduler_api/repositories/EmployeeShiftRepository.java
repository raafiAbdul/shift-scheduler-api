package com.projects.shift_scheduler_api.repositories;

import com.projects.shift_scheduler_api.models.EmployeeShift;
import com.projects.shift_scheduler_api.models.EmployeeShiftKey;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeShiftRepository extends CrudRepository<EmployeeShift, EmployeeShiftKey> {
}
