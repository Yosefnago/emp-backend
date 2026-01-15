package com.ms.sw.user.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record EventDto(
        String eventName,
        LocalDate eventDate,
        LocalTime eventTime,
        String priority,
        String description,
        String location,
        int numberOfAttendance
) {
}
