package com.example.UfaTest.model;

import com.example.UfaTest.Enum.ReservationStatus;
import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "Reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "visit_slot_id")
    private VisitSlot visitSlot;

    private int durationTime = 1;

    private LocalTime reservationTime = LocalTime.now();

    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.ACTIVE;  // Статус записи
}


