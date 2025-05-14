package com.example.UfaTest.DTO;

import java.time.LocalTime;


public class VisitSlotDTO {
    private Long id; //id слота
    private LocalTime startTime;
    private int maxCapacityReservations; // макс вместимость
    private int countReservations;

    VisitSlotDTO() {
    }

    public VisitSlotDTO(Long id, LocalTime startTime, int maxCapacityReservations, int countReservations) {
        this.id = id;
        this.startTime = startTime;
        this.maxCapacityReservations = maxCapacityReservations;
        this.countReservations = countReservations;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public int getMaxCapacityReservations() {
        return maxCapacityReservations;
    }

    public void setMaxCapacityReservations(int maxCapacityReservations) {
        this.maxCapacityReservations = maxCapacityReservations;
    }

    public int getCountReservations() {
        return countReservations;
    }

    public void setCountReservations(int countReservations) {
        this.countReservations = countReservations;
    }

    @Override
    public String toString() {
        return "VisitSlotDTO{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", maxCapacityReservations=" + maxCapacityReservations +
                ", countReservations=" + countReservations +
                '}';
    }
}
