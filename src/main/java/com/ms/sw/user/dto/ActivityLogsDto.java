package com.ms.sw.user.dto;

import com.ms.sw.user.model.ActionType;

import java.time.LocalDate;
import java.time.LocalTime;

public record ActivityLogsDto(
        String fromUser,
        ActionType action,
        String affectedEmployee,
        LocalDate dateAction,
        LocalTime timeAction
) {
}
