package com.example.UfaTest.exeptions;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class NoAvailableSlotsException extends RuntimeException {

    public NoAvailableSlotsException(LocalTime slotTime) {
        super(String.format("Нет свободных мест на %s", slotTime.format(DateTimeFormatter.ISO_LOCAL_TIME)));
    }
}
