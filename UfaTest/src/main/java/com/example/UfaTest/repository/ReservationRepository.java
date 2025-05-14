package com.example.UfaTest.repository;

import com.example.UfaTest.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Modifying
    @Query("UPDATE Reservation r SET r.status = 'CANCELLED' WHERE r.id = :id")
    int cancelReservation(@Param("id") Long id);


    List<Reservation> findByClientId(Long clientId);


}
