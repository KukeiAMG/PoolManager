package com.example.UfaTest.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "VisitSlot")
public class VisitSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime startTime;

    private LocalTime endTime;

    private LocalDate visitDate;

    private int maxCapacityReservations = 10;

    private int countReservations = 0;

    @OneToMany(mappedBy = "visitSlot", cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();
}
