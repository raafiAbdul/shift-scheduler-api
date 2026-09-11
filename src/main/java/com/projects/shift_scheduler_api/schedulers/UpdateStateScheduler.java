package com.projects.shift_scheduler_api.schedulers;

import com.projects.shift_scheduler_api.models.*;
import com.projects.shift_scheduler_api.repositories.ShiftRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Component
public class UpdateStateScheduler {
    private final ShiftRepository shiftRepository;

    public UpdateStateScheduler(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }


    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void updateShiftState() {

        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime daysAgo = now.minusDays(3L);

        List<Long> shiftIdList = shiftRepository.findNotClosedIdsByTimeInBetweenExceptFuture(daysAgo, now);

        List<Shift> shiftList = shiftRepository.findFullyHydratedShiftById(shiftIdList);

        for(Shift s : shiftList) {

            if(now.isAfter(s.getStartTime()) && now.isBefore(s.getEndTime())) {
                s.setState(ShiftState.IN_PROGRESS);
                continue;
            }

            if(now.isAfter(s.getEndTime()))
                s.setState(ShiftState.CLOSED);

            for(EmployeeShift es : s.getEmployeeShifts()) {
                if(es.getClockedOut() == null)
                    continue;

                if(es.getEmployee() instanceof Worker w) {
                    BigDecimal hoursWorked = BigDecimal.valueOf(es.getHoursWorked());
                    BigDecimal earnings = w.getPayRate();
                    w.setBalance(w.getBalance().add(hoursWorked.multiply(earnings)));
                }
            }
        }

    }
}
