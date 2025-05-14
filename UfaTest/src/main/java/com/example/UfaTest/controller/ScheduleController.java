package com.example.UfaTest.controller;

import com.example.UfaTest.exeptions.NoAvailableSlotsException;
import com.example.UfaTest.model.Client;
import com.example.UfaTest.model.Day;
import com.example.UfaTest.model.Reservation;
import com.example.UfaTest.model.VisitSlot;
import com.example.UfaTest.service.ClientService;
import com.example.UfaTest.service.DayService;
import com.example.UfaTest.service.ReservationService;
import com.example.UfaTest.service.VisitSlotService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v0/timetable")
public class ScheduleController {

    private final DayService dayService;
    private final VisitSlotService visitSlotService;
    private final ClientService clientService;
    private final ReservationService reservationService;

    public ScheduleController(DayService dayService, VisitSlotService visitSlotService, ClientService clientService, ReservationService reservationService) {
        this.dayService = dayService;
        this.visitSlotService = visitSlotService;
        this.clientService = clientService;
        this.reservationService = reservationService;
    }

    // добавить день
    @PostMapping("/day/add")
    public ResponseEntity<?> addDay(@RequestBody Day dayRequest) {
        // Проверяем обязательное поле даты
        try {
            if (dayRequest.getLocalDate() == null) {
                return ResponseEntity.badRequest().body("Поле localDate обязательно для заполнения");
            }

            //Сначала создаем и сохраняем день
            Day savedDay = dayService.addDay(dayRequest);

            // Генерируем слоты и устанавливаем связь с днем
            List<VisitSlot> slots = visitSlotService.generateVisitSlotsForDay(savedDay);

            // Добавляем слоты к дню
            savedDay.getVisitSlotList().clear(); // Очищаем существующую коллекцию
            savedDay.getVisitSlotList().addAll(slots); // Добавляем новые элементы

            // Обновляем день с привязанными слотами
            return ResponseEntity.ok(dayService.updateDay(savedDay));
        } catch (IllegalArgumentException | DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // записаться пользователю
    @PostMapping("/reserve")
    public ResponseEntity<?> addReservation(@RequestBody Map<String, String> reservationInfo) {
        try {
            if (!reservationInfo.containsKey("clientId") || !reservationInfo.containsKey("date") || !reservationInfo.containsKey("time")) {
                return ResponseEntity.badRequest().body("Не указаны обязательные поля: clientId(int), date(yyyy-MM-dd), time(hh:mm)");
            }

            Client client = clientService.getClientById(Long.parseLong(reservationInfo.get("clientId")));
            Day day = dayService.getDayByLocalDate(reservationInfo.get("date"));
            VisitSlot visitSlot = visitSlotService.getVisitSlotByStartTime(reservationInfo.get("time"), day.getId());
            Reservation reservation = new Reservation(client, visitSlot);

            // для проверки на то что кол-во регистраций клиента не превышает допустимое кол-во
            boolean isRegistered = reservationService.isUserRegisteredForDay(day, client);
            if (isRegistered) {
                throw new DataIntegrityViolationException("Клиент с Id(" + reservationInfo.get("clientId")
                        + ") на день(" + reservationInfo.get("date") + ") уже записан");
            }
            return ResponseEntity.ok(reservationService.addReservation(reservation));

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (NoAvailableSlotsException e) {
            // Обработка кастомного исключения
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // 409 Conflict
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("clientId должен быть числом");
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Неверный формат даты или времени");
        } catch (EntityNotFoundException e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Произошла ошибка");
        }

    }

    // получить все VisitSlot и кол-во занятых мест в них
    @GetMapping("/all")
    public ResponseEntity<?> getAll(@RequestBody Map<String, String> date) {
        try {
            // Проверка наличия даты в запросе
            if (!date.containsKey("date")) {
                throw new IllegalArgumentException("Не указано обязательное поле: date(YYYY-MM-DD)");
            }
            // берем сущность по дате
            Day day = dayService.getDayByLocalDate(date.get("date"));
            return ResponseEntity.ok(visitSlotService.getBusyVisitSlotsForDate(day));

        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Неверный формат даты или времени");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // получить свободные VisitSlot на определенный день
    @GetMapping("/available")
    public ResponseEntity<?> getAvailable(@RequestBody Map<String, String> date) {
        try {
            if (!date.containsKey("date")) {
                return ResponseEntity.badRequest().body("Не указано обязательное поле: date(YYYY-MM-DD)");
            }
            Day day = dayService.getDayByLocalDate(date.get("date"));
            return ResponseEntity.ok(visitSlotService.getAvailableVisitSlotsForDate(day));
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

    }

    // отмен записи
    @PostMapping("/cancel")
    public ResponseEntity<?> cancelReservation(@RequestBody Map<String, String> reservationInfo) {
        try {
            if (!reservationInfo.containsKey("clientId") || !reservationInfo.containsKey("orderId")) {
                return ResponseEntity.badRequest().body("Не указаны обязательные поля: clientId(int), orderId(int)");
            }

            // вообще передача id клиента здесь не нужна, тк в БД все ти сущности связаны и достаточно только orderId
            // и можно спокойно его убрать и ничего не сломается))
            Long clientId = Long.parseLong(reservationInfo.get("clientId"));
            Long orderId = Long.parseLong(reservationInfo.get("orderId"));
            reservationService.cancelReservation(clientId, orderId);

            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) { // если не найден клиент или запись
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) { // если запись уже отменена
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (NumberFormatException e) { // если передадут id не int
            return ResponseEntity.badRequest().body("Неверный формат id(int)");
        }

    }

    // получение записей клиента по ФИО
    @GetMapping("/reserveByFullName")
    public ResponseEntity<?> getReserveByFullName(@RequestBody Map<String, String> clientInfo) {
        if (!clientInfo.containsKey("name") || !clientInfo.containsKey("surname") || !clientInfo.containsKey("patronymic")) {
            return ResponseEntity.badRequest().body("Не указаны обязательные поля: name, surname, patronymic");
        }
        try {
            String name = clientInfo.get("name");
            String surname = clientInfo.get("surname");
            String patronymic = clientInfo.get("patronymic");
            Client client = clientService.getClientByFullname(name, surname, patronymic);

            return ResponseEntity.ok().body(reservationService.getReservationsByClient(client));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }


    // получение записей клиентов на определенную дату
    // {"date":"2025-05-09"}
    @GetMapping("/reserveByDate")
    public ResponseEntity<?> getReserveByDate(@RequestBody Map<String, String> dateInfo) {
        try {
            if (!dateInfo.containsKey("date")) {
                throw new IllegalArgumentException("Не указано обязательное поле: date(YYYY-MM-DD)");
            }
            LocalDate date = LocalDate.parse(dateInfo.get("date"));
            return ResponseEntity.ok().body(reservationService.getReserveByDate(date));
        } catch (IllegalArgumentException e) {
            // Некорректный формат даты или отсутствие обязательного поля
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DateTimeParseException e) {
            // Ошибка парсинга даты
            return ResponseEntity.badRequest().body("Неверный формат даты. Ожидается YYYY-MM-DD");
        } catch (EntityNotFoundException e) {
            // День не найден в базе
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }
}
