package com.ms.sw.views.controller;

import com.ms.sw.config.customUtils.CurrentUser;
import com.ms.sw.user.model.User;
import com.ms.sw.user.service.ActivityLogsService;
import com.ms.sw.views.dto.ActivityLogsListDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for accessing user activity logs.
 *
 * <p>Provides endpoints to retrieve all audit logs for the authenticated user.</p>
 */
@RestController
@RequestMapping("logs")
public class AuditLogsController {

    private final ActivityLogsService activityLogsService;

    public AuditLogsController(ActivityLogsService activityLogsService) {
        this.activityLogsService = activityLogsService;
    }

    /**
     * Retrieves all activity logs for the authenticated user.
     *
     * @param user the currently authenticated user (injected via @CurrentUser)
     * @return {@link ResponseEntity} containing a list of {@link ActivityLogsListDto}
     *         representing all activities performed by or related to the user
     */
    @GetMapping("/all")
    public ResponseEntity<List<ActivityLogsListDto>> getAll(@CurrentUser User user) {

        return ResponseEntity.ok(activityLogsService.getAllActivities(user.getUsername()));
    }

}
