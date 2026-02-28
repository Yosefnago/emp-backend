package com.ms.sw.notifications.controller;

import com.ms.sw.config.customUtils.CurrentUser;
import com.ms.sw.notifications.dto.NotificationResponse;
import com.ms.sw.user.model.User;
import com.ms.sw.notifications.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing user notifications.
 *
 * <p>Provides endpoints to retrieve, update, and delete notifications for the currently
 * authenticated user.</p>
 */
@RestController
@RequestMapping("/notifications")
@Slf4j
public class NotificationController {


    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Retrieves all notifications for the authenticated user.
     *
     * @param user the currently authenticated user
     * @return list of {@link NotificationResponse} objects
     */
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications(@CurrentUser User user) {
        log.info("GET /notifications/ -> getNotifications -> user={}",user.getUsername());
        List<NotificationResponse> notifications = notificationService.getUserNotifications(user.getUsername());
        return ResponseEntity.ok(notifications);
    }

    /**
     * Retrieves the count of unread notifications for the authenticated user.
     *
     * @param user the currently authenticated user
     * @return number of unread notifications
     */
    @GetMapping("/unread-count")
    public ResponseEntity<Integer> getUnreadCount(@CurrentUser User user) {
        log.info("GET /notifications/count -> getUnreadCount -> user={}",user.getUsername());

        int count = notificationService.getUnreadCount(user.getUsername());
        return ResponseEntity.ok(count);
    }

    /**
     * Marks a specific notification as read.
     *
     * @param user the currently authenticated user
     * @param id ID of the notification to mark as read
     * @return HTTP 200 OK on success
     */
    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@CurrentUser User user, @PathVariable Long id) {
        log.info("PUT /notifications/{}/read -> getUnreadCount -> user={}",id,user.getUsername());

        notificationService.markAsRead(id, user.getUsername());
        return ResponseEntity.ok().build();
    }

    /**
     * Marks all notifications as read for the authenticated user.
     *
     * @param user the currently authenticated user
     * @return HTTP 200 OK on success
     */
    @PutMapping("/mark-all-read")
    public ResponseEntity<Void> markAllAsRead(@CurrentUser User user) {
        log.info("PUT /notifications/mark-all-read -> markAllAsRead -> user={}",user.getUsername());

        notificationService.markAllAsRead(user.getUsername());
        return ResponseEntity.ok().build();
    }

    /**
     * Deletes a specific notification.
     *
     * @param user the currently authenticated user
     * @param id ID of the notification to delete
     * @return HTTP 200 OK on success
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@CurrentUser User user, @PathVariable Long id) {
        log.info("DELETE /notifications/{} -> deleteNotification -> user={}",id,user.getUsername());

        notificationService.deleteNotification(id, user.getUsername());
        return ResponseEntity.ok().build();
    }

    /**
     * Deletes all notifications for the authenticated user.
     *
     * @param user the currently authenticated user
     * @return HTTP 200 OK on success
     */
    @DeleteMapping("/clear-all")
    public ResponseEntity<Void> clearAll(@CurrentUser User user) {
        log.info("DELETE /notifications/clear-all -> clearAll -> user={}",user.getUsername());

        notificationService.clearAll(user.getUsername());
        return ResponseEntity.ok().build();
    }
}
