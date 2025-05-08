package com.example.UfaTest.service;


import com.example.UfaTest.model.Client;
import com.example.UfaTest.model.Day;
import com.example.UfaTest.model.Reservation;
import com.example.UfaTest.model.VisitSlot;
import com.example.UfaTest.repository.DayRepository;
import com.example.UfaTest.repository.ReservationRepository;
import com.example.UfaTest.repository.VisitSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class ScheduleService {

    private final DayRepository dayRepository;
    private final ReservationRepository reservationRepository;
    private final VisitSlotRepository visitSlotRepository;


    public ScheduleService(DayRepository dayRepository, ReservationRepository reservationRepository, VisitSlotRepository visitSlotRepository) {
        this.dayRepository = dayRepository;
        this.reservationRepository = reservationRepository;
        this.visitSlotRepository = visitSlotRepository;
    }

    @Transactional
    public Day addDay(Day day){
         //сохраняем модель конфиг дня в бд
        return dayRepository.save(day);
    }

    @Transactional
    public Day updateDay(Day day){
        return dayRepository.save(day);
    }

    public Day getDayByLocalDate(String localDateString){

        LocalDate localDate = LocalDate.parse(localDateString);
        return dayRepository.findByLocalDate(localDate).orElseThrow(() -> new RuntimeException("Day с датой не найден: " + localDate));
    }

    public List<VisitSlot> generateVisitSlotsForDay(Day day){

        LocalTime startTime = day.getWorkingHoursStart();
        LocalTime endTime = day.getWorkingHoursEnd();
        List<VisitSlot> visitSlotList = new ArrayList<>();

        while(startTime.isBefore(endTime)){
            VisitSlot visitSlot = new VisitSlot(startTime, day.getMaxCapacityReservations());
            visitSlot.setDay(day);
            startTime = startTime.plusHours(1);
            visitSlotList.add(visitSlot);
        }
        return visitSlotList;
    }

    @Transactional
    public Reservation addReservation(Reservation reservation){
        return reservationRepository.save(reservation);
    }

    public VisitSlot getVisitSlotByStartTime(String startTimeString, Long dayId){

        LocalTime startTime = LocalTime.parse(startTimeString);

        VisitSlot visitSlot = visitSlotRepository.findByStartTimeAndDayId(startTime, dayId).orElseThrow(() -> new RuntimeException("VisitSlot с таким временем не найден: " + startTime));
        return visitSlot;
    }


//    public void addVisitSlots(Day day){
//        LocalTime startTime = day.getWorkingHoursStart();
//        LocalTime endTime = day.getWorkingHoursEnd();
//
//
//        while(startTime.isBefore(endTime)){
//            VisitSlot visitSlot = new VisitSlot(startTime, day.getMaxCapacityReservations());
//            visitSlotRepository.save(visitSlot);
//            startTime = startTime.plusHours(1);
//        }
//    }
}
