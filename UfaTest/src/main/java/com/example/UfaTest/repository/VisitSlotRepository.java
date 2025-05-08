package com.example.UfaTest.repository;

import com.example.UfaTest.model.VisitSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.Optional;

public interface VisitSlotRepository extends JpaRepository<VisitSlot, Long> {
    Optional<VisitSlot> findByStartTimeAndDayId(LocalTime startTime, Long dayId);
}
