package com.ms.sw.controller;

import com.ms.sw.Dto.DashboardStatsResponse;
import com.ms.sw.customUtils.CurrentUser;
import com.ms.sw.entity.User;
import com.ms.sw.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("dashboard")
@Slf4j
public class DashboardController {

    DashboardService dashboardService;
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats(@CurrentUser User user){

        log.info("Get Dashboard Stats invoked by user '{}'", user.getUsername());
        return ResponseEntity.ok(dashboardService.loadDashboardData(user.getUsername()));
    }

}
