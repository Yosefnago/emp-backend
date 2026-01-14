package com.ms.sw.Dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ActivityLogsDto(
        String fromUser,
        String action,
        LocalDate actionDate,
        LocalTime actionTime
) {
}
