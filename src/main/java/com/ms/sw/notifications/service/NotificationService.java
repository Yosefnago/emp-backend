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

/**
 * Service for managing user notifications.
 *
 * <p>Provides methods to create, retrieve, update, and delete notifications.
 * Supports event reminders, today's events, and birthday reminders.</p>
 */
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * Creates a notification for the given user.
     *
     * @param user    user to receive the notification
     * @param message notification message
     * @param type    notification type
     */
    private void createNotification(User user, String message, NotificationType type) {
        Notifications notification = new Notifications();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setType(type.name());
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);
    }

    /**
     * Creates a reminder notification for an upcoming event.
     *
     * @param user       user to receive the reminder
     * @param eventTitle title of the event
     * @param daysUntil  days remaining until the event
     * @param eventDate  event date in string format
     */
    public void createEventReminder(User user, String eventTitle, int daysUntil, String eventDate) {
        String message = String.format(
                NotificationMessages.EVENT_UPCOMING_REMINDER,
                eventTitle,
                daysUntil,
                eventDate
        );
        createNotification(user, message, NotificationType.EVENT_REMINDER);
    }

    /**
     * Creates a notification for an event happening today.
     *
     * @param user       user to receive the notification
     * @param eventTitle title of the event
     * @param eventTime  time of the event
     */
    public void createEventTodayNotification(User user, String eventTitle, String eventTime) {
        String message = String.format(
                NotificationMessages.EVENT_TODAY,
                eventTitle,
                eventTime
        );
        createNotification(user, message, NotificationType.EVENT_TODAY);
    }

    /**
     * Creates a birthday reminder notification.
     *
     * @param user         user to receive the notification
     * @param employeeName employee's name
     * @param birthDate    employee's birth date
     */
    public void createBirthdayReminder(User user, String employeeName, String birthDate) {
        String message = String.format(
                NotificationMessages.BIRTHDAY_REMINDER,
                employeeName,
                birthDate
        );
        createNotification(user, message, NotificationType.BIRTHDAY_REMINDER);
    }

    /**
     * Retrieves all notifications for the given username.
     *
     * @param username user whose notifications are retrieved
     * @return list of {@link NotificationResponse} objects
     */
    public List<NotificationResponse> getUserNotifications(String username) {
        List<Notifications> notifications = notificationRepository.findAllByUsername(username);

        return notifications.stream()
                .map(this::mapToNotificationResponse)
                .toList();
    }

    /**
     * Counts unread notifications for the given user.
     *
     * @param username user whose unread notifications are counted
     * @return number of unread notifications
     */
    public int getUnreadCount(String username) {
        return notificationRepository.countUnreadByUsername(username);
    }

    /**
     * Marks a specific notification as read.
     *
     * @param id       notification ID
     * @param username owner of the notification
     * @throws RuntimeException if notification is not found or unauthorized
     */
    @Transactional
    public void markAsRead(Long id, String username) {
        int updated = notificationRepository.markAsReadByIdAndUsername(id, username);

        if (updated == 0) {
            throw new RuntimeException("Notification not found or unauthorized");
        }
    }

    /**
     * Marks all notifications for a user as read.
     *
     * @param username user whose notifications are updated
     */
    @Transactional
    public void markAllAsRead(String username) {
        notificationRepository.markAllAsReadByUsername(username);
    }

    /**
     * Deletes a specific notification.
     *
     * @param id       notification ID
     * @param username owner of the notification
     * @throws RuntimeException if notification is not found or unauthorized
     */
    @Transactional
    public void deleteNotification(Long id, String username) {
        int deleted = notificationRepository.deleteByIdAndUsername(id, username);

        if (deleted == 0) {
            throw new RuntimeException("Notification not found or unauthorized");
        }
    }

    /**
     * Deletes all notifications for a user.
     *
     * @param username user whose notifications are cleared
     */
    @Transactional
    public void clearAll(String username) {
        notificationRepository.deleteAllByUsername(username);
    }

    /**
     * Maps a {@link Notifications} entity to a {@link NotificationResponse} DTO.
     *
     * @param notification notification entity
     * @return mapped {@link NotificationResponse}
     */
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
