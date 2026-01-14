package com.ms.sw.Dto;

import java.math.BigDecimal;

public record DashboardStatsResponse(
        int numberOfEmployees,
        int numberOfAttendants,
        int projectsOnboard
) {
}
