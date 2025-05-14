package com.example.UfaTest.model;

import com.example.UfaTest.Enum.DayType;
import com.fasterxml.jackson.annotation.JsonFormat;
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


    @Column(nullable = false, unique = true)
    private LocalDate localDate; // дата на которую надо составить расписание

    @Column(nullable = false)
    @JsonFormat(pattern = "H:mm")
    private LocalTime workingHoursStart; // (начало рабочего дня)

    @Column(nullable = false)
    @JsonFormat(pattern = "H:mm")
    private LocalTime workingHoursEnd; // (конец рабочего дня)

    private String dayType; // тип дня, по умолчанию рабочий

    private int maxCapacityReservations; // максимальное кол-во посетителей в слоте

    private int maxVisitsPerDay = 1;

    @OneToMany(mappedBy = "day", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "day-visitSlot")
    private List<VisitSlot> visitSlotList;

    public Day() {
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

    // создание стандартного рабочего дня
    public static Day createDefaultWorkDay(LocalDate date) {
        Day day = new Day();
        day.setLocalDate(date);
        day.setWorkingHoursStart(LocalTime.parse("08:00"));
        day.setWorkingHoursEnd(LocalTime.parse("22:00"));
        day.setDayType(DayType.WORKDAY.toString());
        day.setMaxCapacityReservations(10);
        day.setMaxVisitsPerDay(1);
        day.setVisitSlotList(new ArrayList<>());
        return day;
    }

    // создание стандартного выходного дня
    public static Day createDefaultHoliday(LocalDate date) {
        Day day = new Day();
        day.setLocalDate(date);
        day.setWorkingHoursStart(LocalTime.parse("10:00"));
        day.setWorkingHoursEnd(LocalTime.parse("23:00"));
        day.setDayType(DayType.HOLIDAY.toString());
        day.setMaxCapacityReservations(15);
        day.setMaxVisitsPerDay(1);
        day.setVisitSlotList(new ArrayList<>());
        return day;
    }

    public Long getId() {
        return id;
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
                '}' + "\n";
    }
}
