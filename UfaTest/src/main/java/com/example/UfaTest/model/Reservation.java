package com.example.UfaTest.model;

import com.example.UfaTest.Enum.ReservationStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference(value = "client-reservations")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "visit_slot_id")
    @JsonBackReference(value = "visitSlot-reservations")
    private VisitSlot visitSlot;

    private LocalTime reservationTime;  //время в которое клиент зарегистрировался

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;  // Статус записи

    public Reservation() {
    }

    public Reservation(Client client, VisitSlot visitSlot) {
        this.client = client;
        this.visitSlot = visitSlot;
        this.reservationTime = LocalTime.now();
        this.status = status = ReservationStatus.ACTIVE;
    }

    public Long getId() {
        return id;
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
                ", reservationTime=" + reservationTime +
                ", status=" + status +
                '}' + "\n";
    }
}


