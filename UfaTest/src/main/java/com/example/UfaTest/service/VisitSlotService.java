package com.example.UfaTest.service;

import com.example.UfaTest.model.Day;
import com.example.UfaTest.model.VisitSlot;
import com.example.UfaTest.repository.VisitSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class VisitSlotService {

    private final VisitSlotRepository visitSlotRepository;

    public VisitSlotService(VisitSlotRepository visitSlotRepository) {
        this.visitSlotRepository = visitSlotRepository;
    }

    public List<VisitSlot> generateVisitSlotsForDay(Day day) {

        if (day == null) {
            throw new RuntimeException("Day не может быть null");
        }
        LocalTime startTime = day.getWorkingHoursStart();
        LocalTime endTime = day.getWorkingHoursEnd();
        List<VisitSlot> visitSlotList = new ArrayList<>();

        while (startTime.isBefore(endTime)) {
            VisitSlot visitSlot = new VisitSlot(startTime, day.getMaxCapacityReservations());
            visitSlot.setDay(day);
            startTime = startTime.plusHours(1);
            visitSlotList.add(visitSlot);
        }
        System.out.println(visitSlotList);
        return visitSlotList;
    }

    public VisitSlot getVisitSlotByStartTime(String startTimeString, Long dayId) {

        LocalTime startTime = LocalTime.parse(startTimeString);

        VisitSlot visitSlot = visitSlotRepository.findByStartTimeAndDayId(startTime, dayId).orElseThrow(() -> new RuntimeException("VisitSlot с таким временем не найден: " + startTime));
        return visitSlot;
    }

    @Transactional(readOnly = true)
    public Map<String, String> getBusyVisitSlotsForDate(Day day) {

        // создаем мапу куда будем записывать время и кол-во занятых мест
        Map<String, String> mapTimeCount = new TreeMap<>(); // TreeMap чтобы значения сортировались по ключу (по времени)

        // Проверка и обработка слотов
        if (day.getVisitSlotList() != null) {
            for (VisitSlot visitSlot : day.getVisitSlotList()) {
                try {
                    String time = visitSlot.getStartTime().toString();
                    String count = String.valueOf(visitSlot.getCountReservations());
                    mapTimeCount.put(time, count);
                } catch (NullPointerException e) {
                    // Пропускаем слоты с некорректными данными
                    continue;
                }
            }
        }
        return mapTimeCount;
    }

    @Transactional(readOnly = true)
    public Map<String, String> getAvailableVisitSlotsForDate(Day day) {
        Map<String, String> mapTimeCount = new TreeMap<>(); // TreeMap чтобы значения сортировались по ключу (по времени)

        for (VisitSlot visitSlot : day.getVisitSlotList()) { // перебор визит слотов в указанном дне
            String time = visitSlot.getStartTime().toString();
            String countReservations = "" + (visitSlot.getMaxCapacityReservations() - visitSlot.getCountReservations());
            if (Integer.parseInt(countReservations) != 0) {
                mapTimeCount.put(time, countReservations); // помещаем время слота и кол-во занятых мест
            }

        }
        return mapTimeCount;
    }

}
