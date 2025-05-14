package com.example.UfaTest.service;

import com.example.UfaTest.Enum.ReservationStatus;
import com.example.UfaTest.exeptions.NoAvailableSlotsException;
import com.example.UfaTest.model.Client;
import com.example.UfaTest.model.Day;
import com.example.UfaTest.model.Reservation;
import com.example.UfaTest.model.VisitSlot;
import com.example.UfaTest.repository.ClientRepository;
import com.example.UfaTest.repository.DayRepository;
import com.example.UfaTest.repository.ReservationRepository;
import com.example.UfaTest.repository.VisitSlotRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final VisitSlotRepository visitSlotRepository;
    private final ClientRepository clientRepository;
    private final DayRepository dayRepository;

    public ReservationService(ReservationRepository reservationRepository, VisitSlotRepository visitSlotRepository, ClientRepository clientRepository, DayRepository dayRepository) {
        this.reservationRepository = reservationRepository;
        this.visitSlotRepository = visitSlotRepository;
        this.clientRepository = clientRepository;
        this.dayRepository = dayRepository;
    }

    @Transactional
    public Reservation addReservation(Reservation reservation) {
        VisitSlot visitSlot = reservation.getVisitSlot();
        int availableSlots = visitSlot.getMaxCapacityReservations() - visitSlot.getCountReservations();

        if (availableSlots <= 0) {
            throw new NoAvailableSlotsException(visitSlot.getStartTime());
        }

        visitSlot.addToCountReservations(1);
        return reservationRepository.save(reservation);
    }

    @Transactional
    public void cancelReservation(Long clientId, Long orderId) {

        // проверить есть ли клиент с таким id
        // проверить есть ли reservation с таким id
        // изменить в этом reservation статус на CANCELED

        // проверяем существует ли клиент с таким Id,
        clientRepository.findById(clientId).orElseThrow(() ->
                new EntityNotFoundException("Клиент с id: " + clientId + " не найден"));

        // проверяем существует ли запись с таким id
        Reservation reservation = reservationRepository.findById(orderId).orElseThrow(() ->
                new EntityNotFoundException("Запись с id : " + orderId + " не найдена"));

        // проверяем не является ли статус уже CANCELED
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("Бронирование уже отменено");
        }

        VisitSlot visitSlot = reservation.getVisitSlot();
        visitSlot.addToCountReservations(-1);
        reservationRepository.cancelReservation(orderId);
    }

    @Transactional(readOnly = true)
    public List<Reservation> getReservationsByClient(Client client) {
        return reservationRepository.findByClientId(client.getId());
    }

    @Transactional(readOnly = true)
    public List<Reservation> getReserveByDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("date не может быть null");
        }

        Day day = dayRepository.findByLocalDate(date).orElseThrow(() -> new RuntimeException("Day с датой не найден: " + date));

        List<Reservation> reservationList = new ArrayList<>();

        if (day.getVisitSlotList() != null) {
            for (VisitSlot visitSlot : day.getVisitSlotList()) {
                for (Reservation reservation : visitSlot.getReservations()) {
                    reservationList.add(reservation);
                }
            }
        }
        return reservationList;
    }

    @Transactional(readOnly = true)
    public boolean isUserRegisteredForDay(Day day, Client client) {
        int count = 1;
        // перебор визитслотов в дне
        for (VisitSlot visitSlot : day.getVisitSlotList()) {
            try {
                // перебор записей в визитслоте
                for (Reservation reservation : visitSlot.getReservations()) {
                    //если запись активна
                    if (reservation.getStatus() == ReservationStatus.ACTIVE) {
                        // если в записи айди клиента совпадает с клиентом который хочет записаться
                        if (reservation.getClient().getId() == client.getId()) {
                            // добавить кол-во посещений за день
                            count++;
                            //если это кол-во превышает разрешенное кол-во в этот день, то гг
                            if (count > day.getMaxVisitsPerDay()) {
                                return true;
                            }
                        }
                    }
                }
            } catch (NullPointerException e) {
                // Пропускаем слоты с некорректными данными
                continue;
            }
        }
        return false;
    }
}
