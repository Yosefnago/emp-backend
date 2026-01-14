package com.ms.sw.views.dto;

public record DashboardStatsResponse(
        int numberOfEmployees,
        int numberOfAttendants,
        int projectsOnboard
) {
}
