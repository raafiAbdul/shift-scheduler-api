package com.projects.shift_scheduler_api.repositories;

import com.projects.shift_scheduler_api.models.Shift;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface ShiftRepository extends CrudRepository<Shift, Long>,
        PagingAndSortingRepository<Shift, Long> {
    @EntityGraph(attributePaths = {"employeeShifts", "employeeShifts.employee"})
    @Query(value = "select s from Shift s " +
            "left join s.employeeShifts es " +
            "left join es.employee e " +
            "where s.id in (select s2.id from Shift s2 " +
            "left join s2.employeeShifts es2 " +
            "left join es2.employee e2 " +
            "where lower(e2.username) " +
            "like lower(concat('%', :user, '%')))",
            countQuery = "select count(s) from Shift s " +
                    "join s.employeeShifts es " +
                    "join es.employee e " +
                    "where lower(e.username) like " +
                    "lower(concat('%', :user, '%'))")
    Page<Shift> findByUser(@Param(value = "user")String user, Pageable pageable);

    @Query(value = "select s from Shift s where " +
            "s.startTime <= :end and s.endTime >= :start")
    Page<Shift> findByTimeInBetween(
            @Param(value = "start")OffsetDateTime start,
            @Param(value = "end") OffsetDateTime end,
            Pageable pageable);

    @Query(value = "delete from employee_shift; delete from shifts", nativeQuery = true)
    @Modifying
    void deleteBulk();

    @Query(value = "select s from Shift s " +
            "where lower(s.description) like " +
            "lower(concat('%', :desc, '%'))")
    Page<Shift> findByDescription(@Param("desc")String desc, Pageable pageable);

    @Query(value = "select s.id from Shift s where " +
            "s.startTime <= :end and s.endTime >= :start " +
            "and s.endTime <= :end and s.state != 'CLOSED'")
    List<Long> findNotClosedIdsByTimeInBetweenExceptFuture(
            @Param(value = "start")OffsetDateTime start,
            @Param(value = "end") OffsetDateTime end);

    @EntityGraph(attributePaths = {"employeeShifts", "employeeShifts.employee"})
    @Query(value = "select s from Shift s where s.id in :idList")
    List<Shift> findFullyHydratedShiftById(@Param(value = "idList")List<Long> idList);
}
