package com.ms.sw.notifications.controller;

import com.ms.sw.config.customUtils.CurrentUser;
import com.ms.sw.notifications.dto.NotificationResponse;
import com.ms.sw.user.model.User;
import com.ms.sw.notifications.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {


    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications(@CurrentUser User user) {
        List<NotificationResponse> notifications = notificationService.getUserNotifications(user.getUsername());
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Integer> getUnreadCount(@CurrentUser User user) {
        int count = notificationService.getUnreadCount(user.getUsername());
        return ResponseEntity.ok(count);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@CurrentUser User user, @PathVariable Long id) {
        notificationService.markAsRead(id, user.getUsername());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/mark-all-read")
    public ResponseEntity<Void> markAllAsRead(@CurrentUser User user) {
        notificationService.markAllAsRead(user.getUsername());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@CurrentUser User user, @PathVariable Long id) {
        notificationService.deleteNotification(id, user.getUsername());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/clear-all")
    public ResponseEntity<Void> clearAll(@CurrentUser User user) {
        notificationService.clearAll(user.getUsername());
        return ResponseEntity.ok().build();
    }
}
