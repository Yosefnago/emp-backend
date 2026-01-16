package com.ms.sw.notifications.service;

import com.ms.sw.notifications.dto.NotificationResponse;
import com.ms.sw.notifications.model.Notifications;
import com.ms.sw.user.model.User;
import com.ms.sw.notifications.repo.NotificationRepository;
import com.ms.sw.notifications.util.NotificationMessages;
import com.ms.sw.notifications.util.NotificationType;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    private Notifications createNotification(User user, String message, NotificationType type) {
        Notifications notification = new Notifications();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setType(type.name());
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        return notificationRepository.save(notification);
    }

    public void createEventReminder(User user, String eventTitle, int daysUntil, String eventDate) {
        String message = String.format(
                NotificationMessages.EVENT_UPCOMING_REMINDER,
                eventTitle,
                daysUntil,
                eventDate
        );
        createNotification(user, message, NotificationType.EVENT_REMINDER);
    }

    public void createEventTodayNotification(User user, String eventTitle, String eventTime) {
        String message = String.format(
                NotificationMessages.EVENT_TODAY,
                eventTitle,
                eventTime
        );
        createNotification(user, message, NotificationType.EVENT_TODAY);
    }

    public void createBirthdayReminder(User user, String employeeName, String birthDate) {
        String message = String.format(
                NotificationMessages.BIRTHDAY_REMINDER,
                employeeName,
                birthDate
        );
        createNotification(user, message, NotificationType.BIRTHDAY_REMINDER);
    }

    public List<NotificationResponse> getUserNotifications(String username) {
        List<Notifications> notifications = notificationRepository.findAllByUsername(username);

        return notifications.stream()
                .map(this::mapToNotificationResponse)
                .toList();
    }


    public int getUnreadCount(String username) {
        return notificationRepository.countUnreadByUsername(username);
    }


    @Transactional
    public void markAsRead(Long id, String username) {
        int updated = notificationRepository.markAsReadByIdAndUsername(id, username);

        if (updated == 0) {
            throw new RuntimeException("Notification not found or unauthorized");
        }
    }


    @Transactional
    public void markAllAsRead(String username) {
        notificationRepository.markAllAsReadByUsername(username);
    }


    @Transactional
    public void deleteNotification(Long id, String username) {
        int deleted = notificationRepository.deleteByIdAndUsername(id, username);

        if (deleted == 0) {
            throw new RuntimeException("Notification not found or unauthorized");
        }
    }


    @Transactional
    public void clearAll(String username) {
        notificationRepository.deleteAllByUsername(username);
    }


    private NotificationResponse mapToNotificationResponse(Notifications notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getMessage(),
                notification.getType(),
                notification.getIsRead(),
                notification.getCreatedAt()
        );
    }
}
