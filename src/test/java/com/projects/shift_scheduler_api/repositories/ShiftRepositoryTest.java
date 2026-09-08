package com.projects.shift_scheduler_api.repositories;

import com.projects.shift_scheduler_api.models.Shift;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShiftRepositoryTest extends InitializeRepositoryTest {
    @Test
    void findByUserTest() {
        Page<Shift> shiftPage = shiftRepository.findByUser("DAVid",
                PageRequest.of(0, 5));
        assertAll(
                () -> assertEquals(3, shiftPage.getNumberOfElements()),
                () -> assertEquals(5, shiftPage.getSize()),
                () -> assertEquals(0, shiftRepository.findByUser("noOne",
                        PageRequest.of(0, 5)).getNumberOfElements())
        );
    }

    @Test
    void findByTimeInBetweenTest() {
        assertAll(
                () -> assertEquals(0, shiftRepository.findByTimeInBetween(
                        OffsetDateTime.of(2026, 2, 1, 0, 0,
                                0, 0, ZoneOffset.UTC),
                        OffsetDateTime.of(2026, 3, 1, 0, 0,
                                0, 0, ZoneOffset.UTC),
                        PageRequest.of(0, 5)).getNumberOfElements()),

                () -> assertEquals(0, shiftRepository.findByTimeInBetween(
                        OffsetDateTime.of(2026, 7, 1, 0, 0,
                                0, 0, ZoneOffset.UTC),
                        OffsetDateTime.of(2026, 7, 2, 0, 0,
                                0, 0, ZoneOffset.UTC),
                        PageRequest.of(0, 5)
                ).getNumberOfElements()),

                () -> assertEquals(1, shiftRepository.findByTimeInBetween(
                        OffsetDateTime.of(2026, 5, 12, 8, 0,
                                0, 0, ZoneOffset.UTC),
                        OffsetDateTime.of(2026, 5, 12, 17, 0,
                                0, 0, ZoneOffset.UTC),
                        PageRequest.of(0, 5)
                ).getNumberOfElements()),

                () -> assertEquals(1, shiftRepository.findByTimeInBetween(
                        OffsetDateTime.of(2026, 5, 12, 4, 0,
                                0, 0, ZoneOffset.UTC),
                        OffsetDateTime.of(2026, 5, 12, 15, 0,
                                0, 0, ZoneOffset.UTC),
                        PageRequest.of(0, 5)
                ).getNumberOfElements()),

                () -> assertEquals(1, shiftRepository.findByTimeInBetween(
                        OffsetDateTime.of(2026, 5, 12, 12, 0,
                                0, 0, ZoneOffset.UTC),
                        OffsetDateTime.of(2026, 5, 12, 20, 0,
                                0, 0, ZoneOffset.UTC),
                        PageRequest.of(0, 5)
                ).getNumberOfElements()),

                () -> assertEquals(1, shiftRepository.findByTimeInBetween(
                        OffsetDateTime.of(2026, 5, 12, 10, 0,
                                0, 0, ZoneOffset.UTC),
                        OffsetDateTime.of(2026, 5, 12, 13, 0,
                                0, 0, ZoneOffset.UTC),
                        PageRequest.of(0, 5)
                ).getNumberOfElements()),

                () -> assertEquals(1, shiftRepository.findByTimeInBetween(
                        OffsetDateTime.of(2026, 5, 12, 13, 0,
                                0, 0, ZoneOffset.UTC),
                        OffsetDateTime.of(2026, 5, 12, 10, 0,
                                0, 0, ZoneOffset.UTC),
                        PageRequest.of(0, 5)
                ).getNumberOfElements())
        );
    }

    @Test
    @Transactional
    void deleteBulkTest() {
        shiftRepository.deleteBulk();
        assertEquals(0, ((List<Shift>)shiftRepository.findAll()).size());
    }

    @Test
    void findByDescriptionTest() {
        assertAll(
                () -> assertEquals(4, shiftRepository.findByDescription("This is the first",
                        PageRequest.of(0, 5)).getNumberOfElements()),
                () -> assertEquals(0, shiftRepository.findByDescription("none",
                        PageRequest.of(0, 5)).getNumberOfElements())
        );
    }

    @Test
    void findNotClosedIdsByTimeInBetweenExceptFutureTest() {
        assertAll(
                () -> assertEquals(1, shiftRepository.findNotClosedIdsByTimeInBetweenExceptFuture(
                        OffsetDateTime.of(2026, 5, 12, 8, 0,
                                0, 0, ZoneOffset.UTC),
                        OffsetDateTime.of(2026, 5, 12, 17, 0,
                                0, 0, ZoneOffset.UTC)
                ).size()),
                () -> assertEquals(1, shiftRepository.findNotClosedIdsByTimeInBetweenExceptFuture(
                        OffsetDateTime.of(2026, 5, 12, 4, 0,
                                0, 0, ZoneOffset.UTC),
                        OffsetDateTime.of(2026, 5, 12, 21, 0,
                                0, 0, ZoneOffset.UTC)
                ).size()),
                () -> assertEquals(1, shiftRepository.findNotClosedIdsByTimeInBetweenExceptFuture(
                        OffsetDateTime.of(2026, 5, 12, 13, 0,
                                0, 0, ZoneOffset.UTC),
                        OffsetDateTime.of(2026, 5, 12, 21, 0,
                                0, 0, ZoneOffset.UTC)
                ).size()),
                () -> assertEquals(0, shiftRepository.findNotClosedIdsByTimeInBetweenExceptFuture(
                        OffsetDateTime.of(2026, 5, 12, 7, 0,
                                0, 0, ZoneOffset.UTC),
                        OffsetDateTime.of(2026, 5, 12, 13, 0,
                                0, 0, ZoneOffset.UTC)
                ).size())
        );
    }

    @Test
    void findFullyHydratedShiftByIdTest() {
        List<Long> idList = List.of(3L, 4L);
        List<Shift> shiftList = shiftRepository.findFullyHydratedShiftById(idList);
        assertAll(
                () -> assertEquals("This is the first shift of July 4, 2026",
                        shiftList.get(0).getDescription()),
                () -> assertEquals("This is the first shift of August 12, 2026",
                        shiftList.get(1).getDescription())
        );
    }
}