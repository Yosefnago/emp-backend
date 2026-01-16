package com.ms.sw.notifications.dto;

import com.dev.tools.Markers.DtoMarker;

import java.time.LocalDateTime;

@DtoMarker
public record NotificationResponse(
        Long id,
        String message,
        String type,
        Boolean isRead,
        LocalDateTime createdAt
) {}
