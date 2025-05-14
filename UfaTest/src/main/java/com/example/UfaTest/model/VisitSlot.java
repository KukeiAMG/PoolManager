package com.example.UfaTest.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "VisitSlot")
public class VisitSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //id слота

    private LocalTime startTime;

    private int maxCapacityReservations; // макс вместимость

    private int countReservations; //кол-во посещений зарегистрированных на этот слот

    @ManyToOne
    @JoinColumn(name = "day_id", nullable = false)
    @JsonBackReference(value = "day-visitSlot")
    private Day day; // параметры дня

    @OneToMany(mappedBy = "visitSlot", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "visitSlot-reservations")
    private List<Reservation> reservations; //  регистрации в этот слот

    public VisitSlot() {
    }

    public VisitSlot(LocalTime startTime, int maxCapacityReservations) {
        this.startTime = startTime;
        this.maxCapacityReservations = maxCapacityReservations;
        this.countReservations = 0;
        this.reservations = new ArrayList<>();
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

    public void addToCountReservations(int num) {
        countReservations += num;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }


    @Override
    public String toString() {
        return "VisitSlot{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", maxCapacityReservations=" + maxCapacityReservations +
                ", countReservations=" + countReservations +
                ", day=" + day +
                ", reservations=" + reservations +
                '}' + "\n";
    }
}
