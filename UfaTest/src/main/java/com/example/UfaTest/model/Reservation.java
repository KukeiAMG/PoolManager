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

    private LocalTime reservationTime = LocalTime.now(); //время в которое клиент зарегистрировался


    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.ACTIVE;  // Статус записи

    public Reservation(){}

    public Reservation(Long id, Client client, VisitSlot visitSlot, int durationTime, LocalTime reservationTime, ReservationStatus status) {
        this.id = id;
        this.client = client;
        this.visitSlot = visitSlot;
        this.durationTime = durationTime;
        this.reservationTime = reservationTime;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public VisitSlot getVisitSlot() {
        return visitSlot;
    }

    public void setVisitSlot(VisitSlot visitSlot) {
        this.visitSlot = visitSlot;
    }

    public int getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(int durationTime) {
        this.durationTime = durationTime;
    }

    public LocalTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", client=" + client +
                ", visitSlot=" + visitSlot +
                ", durationTime=" + durationTime +
                ", reservationTime=" + reservationTime +
                ", status=" + status +
                '}';
    }
}


