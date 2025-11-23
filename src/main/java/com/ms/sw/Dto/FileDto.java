package com.ms.sw.Dto;

import java.time.LocalDateTime;

public record FileDto(
        Long id,
        String name,
        String path,
        LocalDateTime uploadedAt,
        String personalId
) {}