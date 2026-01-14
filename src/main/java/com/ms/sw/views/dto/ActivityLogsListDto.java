package com.ms.sw.views.dto;

import com.ms.sw.user.model.ActionType;

import java.time.LocalDate;
import java.time.LocalTime;

public record ActivityLogsListDto(
        String fromUser,
        ActionType action,
        String affectedEmployee,
        LocalDate dateAction,
        LocalTime timeAction
) {
}
