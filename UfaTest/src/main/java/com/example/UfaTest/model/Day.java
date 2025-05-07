package com.example.UfaTest.model;

import com.example.UfaTest.Enum.DayType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Day")
public class Day {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // (PK)

    @Column(nullable = false)
    private LocalDate localDate ; // дата на которую надо составить расписание

    @Column(nullable = false)
    private LocalTime workingHoursStart ; // (начало рабочего дня)

    @Column(nullable = false)
    private LocalTime workingHoursEnd ; // (конец рабочего дня)

    private String dayType ; // тип дня, по умолчанию рабочий

    private int maxCapacityReservations; // максимальное кол-во посетителей в слоте

    private int maxVisitsPerDay = 1;

    @OneToMany(mappedBy = "day", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<VisitSlot> visitSlotList;

    public Day(){
        this.localDate = LocalDate.parse("2025-05-07");
        this.workingHoursStart = LocalTime.parse("08:00");
        this.workingHoursEnd = LocalTime.parse("22:00");
        this.dayType = DayType.WORKDAY.toString();
        this.maxCapacityReservations = 10;
        this.maxVisitsPerDay = 1;
        this.visitSlotList = new ArrayList<>();
    }

    public Day(LocalDate localDate, LocalTime workingHoursStart, LocalTime workingHoursEnd, String dayType, int maxCapacityReservations, int maxVisitsPerDay, List<VisitSlot> visitSlotList) {
        this.localDate = localDate;
        this.workingHoursStart = workingHoursStart;
        this.workingHoursEnd = workingHoursEnd;
        this.dayType = dayType;
        this.maxVisitsPerDay = maxVisitsPerDay;
        this.maxCapacityReservations = maxCapacityReservations;
        this.visitSlotList = visitSlotList;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public LocalTime getWorkingHoursStart() {
        return workingHoursStart;
    }

    public void setWorkingHoursStart(LocalTime workingHoursStart) {
        this.workingHoursStart = workingHoursStart;
    }

    public LocalTime getWorkingHoursEnd() {
        return workingHoursEnd;
    }

    public void setWorkingHoursEnd(LocalTime workingHoursEnd) {
        this.workingHoursEnd = workingHoursEnd;
    }

    public String getDayType() {
        return dayType;
    }

    public int getMaxCapacityReservations() {
        return maxCapacityReservations;
    }

    public void setMaxCapacityReservations(int maxCapacityReservations) {
        this.maxCapacityReservations = maxCapacityReservations;
    }

    public void setDayType(String dayType) {
        this.dayType = dayType;
    }

    public int getMaxVisitsPerDay() {
        return maxVisitsPerDay;
    }

    public void setMaxVisitsPerDay(int maxVisitsPerDay) {
        this.maxVisitsPerDay = maxVisitsPerDay;
    }

    public List<VisitSlot> getVisitSlotList() {
        return visitSlotList;
    }

    public void setVisitSlotList(List<VisitSlot> visitSlotList) {
        this.visitSlotList = visitSlotList;
    }

    @Override
    public String toString() {
        return "Day{" +
                "maxVisitsPerDay=" + maxVisitsPerDay +
                ", maxCapacityReservations=" + maxCapacityReservations +
                ", dayType='" + dayType + '\'' +
                ", workingHoursEnd=" + workingHoursEnd +
                ", workingHoursStart=" + workingHoursStart +
                ", localDate=" + localDate +
                ", id=" + id +
                '}';
    }
}
