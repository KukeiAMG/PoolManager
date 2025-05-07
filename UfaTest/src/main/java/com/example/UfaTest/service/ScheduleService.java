package com.example.UfaTest.service;


import com.example.UfaTest.model.Day;
import com.example.UfaTest.model.VisitSlot;
import com.example.UfaTest.repository.DayRepository;
import com.example.UfaTest.repository.VisitSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class ScheduleService {

    private final DayRepository dayRepository;
    private final VisitSlotRepository visitSlotRepository;


    public ScheduleService(DayRepository dayRepository, VisitSlotRepository visitSlotRepository) {
        this.dayRepository = dayRepository;
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
