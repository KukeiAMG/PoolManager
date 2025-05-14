package com.example.UfaTest.service;


import com.example.UfaTest.Enum.DayType;
import com.example.UfaTest.model.Day;
import com.example.UfaTest.model.Reservation;
import com.example.UfaTest.repository.DayRepository;
import com.example.UfaTest.repository.ReservationRepository;
import com.example.UfaTest.repository.VisitSlotRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;


@Service
public class DayService {

    private final DayRepository dayRepository;
    private final ReservationRepository reservationRepository;
    private final VisitSlotRepository visitSlotRepository;


    public DayService(DayRepository dayRepository, ReservationRepository reservationRepository, VisitSlotRepository visitSlotRepository) {
        this.dayRepository = dayRepository;
        this.reservationRepository = reservationRepository;
        this.visitSlotRepository = visitSlotRepository;
    }

    @Transactional
    public Day addDay(Day dayRequest) {
        if (dayRequest == null) {
            throw new IllegalArgumentException("Day не может быть null");
        }
        try {
            Day day;

            // Если тип дня HOLIDAY, используем соответствующие значения по умолчанию
            if (dayRequest.getDayType() != null && dayRequest.getDayType().equalsIgnoreCase(DayType.HOLIDAY.toString())) {
                day = Day.createDefaultHoliday(dayRequest.getLocalDate());
                System.out.println(day);
            } else {
                // Иначе используем WORKDAY по умолчанию
                day = Day.createDefaultWorkDay(dayRequest.getLocalDate());
            }

            // Перезаписываем значения по умолчанию, если они указаны в запросе
            if (dayRequest.getWorkingHoursStart() != null) {
                day.setWorkingHoursStart(dayRequest.getWorkingHoursStart());
            }
            if (dayRequest.getWorkingHoursEnd() != null) {
                day.setWorkingHoursEnd(dayRequest.getWorkingHoursEnd());
            }
            if (dayRequest.getMaxCapacityReservations() != 0) {
                day.setMaxCapacityReservations(dayRequest.getMaxCapacityReservations());
            }
            if (dayRequest.getMaxVisitsPerDay() != 0) {
                day.setMaxVisitsPerDay(dayRequest.getMaxVisitsPerDay());
            }
            return dayRepository.save(day);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Передача недопустимых значений");
        } catch (DataIntegrityViolationException e){
            throw new DataIntegrityViolationException("Такой день уже есть");
        }


    }

    @Transactional
    public Day updateDay(Day day) {
        if (day == null) {
            throw new IllegalArgumentException("Day не может быть null");
        }
        try {
            return dayRepository.save(day);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Передача недопустимых значений");
        }

    }

    @Transactional(readOnly = true)
    public Day getDayByLocalDate(String localDateString) {
        // проверяем не является ли строка null или пустой строкой
        if (localDateString == null || localDateString.isBlank()) {
            throw new IllegalArgumentException("Поле localDate не может быть пустой");
        }

        LocalDate localDate;

        try {
            localDate = LocalDate.parse(localDateString);
        } catch (DateTimeParseException e) { // для поимки ошибки если не получится превратить из строки в дату
            throw new IllegalArgumentException("Неверный формат даты. Ожидается формат: YYYY-MM-DD", e);
        }

        return dayRepository.findByLocalDate(localDate).orElseThrow(() -> new EntityNotFoundException("Day с датой " + localDate + " не найден"));
    }

}
