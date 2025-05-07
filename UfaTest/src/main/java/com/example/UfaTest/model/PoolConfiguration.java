package com.example.UfaTest.model;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "PoolConfiguration")
public class PoolConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // (PK)

    @Column(nullable = false)
    private LocalTime workingHoursStart; // (начало рабочего дня)

    @Column(nullable = false)
    private LocalTime workingHoursEnd; // (конец рабочего дня)

    private int maxVisitsPerDay = 1;
}
