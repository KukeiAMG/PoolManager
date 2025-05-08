package com.example.UfaTest.controller;


import com.example.UfaTest.model.Client;
import com.example.UfaTest.model.Day;
import com.example.UfaTest.model.Reservation;
import com.example.UfaTest.model.VisitSlot;
import com.example.UfaTest.service.ClientService;
import com.example.UfaTest.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v0/timetable")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final ClientService clientService;

    public ScheduleController(ScheduleService scheduleService, ClientService clientService) {
        this.scheduleService = scheduleService;
        this.clientService = clientService;
    }

    @PostMapping("/day/add")
    public ResponseEntity<Day> addDay(@RequestBody Day day){
        //TODO
        // добавить логирование

        // 1. Сначала создаем и сохраняем день
        Day savedDay = scheduleService.addDay(day);

        // 2. Генерируем слоты и устанавливаем связь с днем
        List<VisitSlot> slots = scheduleService.generateVisitSlotsForDay(savedDay);

        // 3. Добавляем слоты к дню
        savedDay.getVisitSlotList().clear(); // Очищаем существующую коллекцию
        savedDay.getVisitSlotList().addAll(slots); // Добавляем новые элементы

        // 4. Обновляем день с привязанными слотами

        return ResponseEntity.ok(scheduleService.updateDay(savedDay));
    }

    @PostMapping("/reservation/add")
    public ResponseEntity<Reservation> addReservation(@RequestBody Map<String, String> reservationInfo){

        Client client = clientService.getClientById(Long.parseLong(reservationInfo.get("clientId")));

        Day day = scheduleService.getDayByLocalDate(reservationInfo.get("date"));

        VisitSlot visitSlot = scheduleService.getVisitSlotByStartTime(reservationInfo.get("time"), day.getId());

        Reservation reservation = new Reservation(client, visitSlot);
        return ResponseEntity.ok(scheduleService.addReservation(reservation));
    }
}
