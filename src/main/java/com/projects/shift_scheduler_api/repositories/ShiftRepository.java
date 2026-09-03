package com.projects.shift_scheduler_api.repositories;

import com.projects.shift_scheduler_api.models.Shift;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ShiftRepository extends CrudRepository<Shift, Long>,
        PagingAndSortingRepository<Shift, Long> {

}
