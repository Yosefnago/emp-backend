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

@RestController
@RequestMapping("logs")
public class AuditLogsController {

    private final ActivityLogsService activityLogsService;

    public AuditLogsController(ActivityLogsService activityLogsService) {
        this.activityLogsService = activityLogsService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ActivityLogsListDto>> getAll(@CurrentUser User user) {

        return ResponseEntity.ok(activityLogsService.getAllActivities(user.getUsername()));
    }

}
