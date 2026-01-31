package com.ms.sw.views.controller;

import com.ms.sw.user.dto.ActivityLogsDto;
import com.ms.sw.views.dto.DashboardStatsResponse;
import com.ms.sw.config.customUtils.CurrentUser;
import com.ms.sw.user.model.User;
import com.ms.sw.user.service.ActivityLogsService;
import com.ms.sw.views.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for retrieving dashboard data and recent user activity.
 *
 * <p>Provides endpoints for fetching dashboard statistics and the last activity logs
 * for the authenticated user.</p>
 */
@RestController
@RequestMapping("dashboard")
@Slf4j
public class DashboardController {

    private final DashboardService dashboardService;
    private final ActivityLogsService activityLogsService;

    public DashboardController(DashboardService dashboardService,ActivityLogsService activityLogsService) {
        this.dashboardService = dashboardService;
        this.activityLogsService = activityLogsService;
    }

    /**
     * Retrieves dashboard statistics for the authenticated user.
     *
     * @param user the currently authenticated user (injected via @CurrentUser)
     * @return {@link ResponseEntity} containing {@link DashboardStatsResponse} with
     *         employee count, attendants count, and projects onboard
     */
    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats(@CurrentUser User user){

        log.info("Get Dashboard Stats invoked by user '{}'", user.getUsername());
        return ResponseEntity.ok(dashboardService.loadDashboardData(user.getUsername()));
    }

    /**
     * Retrieves the last few activity logs for the authenticated user.
     *
     * @param user the currently authenticated user (injected via @CurrentUser)
     * @return {@link ResponseEntity} containing a list of {@link ActivityLogsDto}
     *         representing the most recent activities performed by the user
     */
    @GetMapping("/activity")
    public ResponseEntity<List<ActivityLogsDto>> getLastActivity(@CurrentUser User user){

        log.info("Get Activity Logs invoked by user '{}'", user.getUsername());
        return ResponseEntity.ok(activityLogsService.getLastActivity(user.getUsername()));
    }

}
