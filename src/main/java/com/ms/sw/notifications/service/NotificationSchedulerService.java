package com.ms.sw.notifications.service;

import com.ms.sw.employee.model.Employees;
import com.ms.sw.employee.repo.EmployeeRepository;
import com.ms.sw.user.model.Events;
import com.ms.sw.user.repo.EventsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;

/**
 * Service responsible for scheduling and sending automated notifications.
 *
 * <p>Uses Spring's {@link Scheduled} annotations to trigger tasks at specific times:
 * - Event reminders 3 days in advance
 * - Event notifications on the day of the event
 * - Birthday reminders 7 days before</p>
 */
@Service
@Slf4j
public class NotificationSchedulerService {

    private final EventsRepository eventsRepository;
    private final EmployeeRepository employeeRepository;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationSchedulerService(EventsRepository eventsRepository,
                                        EmployeeRepository employeeRepository,
                                        NotificationService notificationService,
                                        SimpMessagingTemplate messagingTemplate) {
        this.eventsRepository = eventsRepository;
        this.employeeRepository = employeeRepository;
        this.notificationService = notificationService;
        this.messagingTemplate = messagingTemplate;
    }
    @Scheduled(cron = "0 14 21 * * ?")
    public void sendNotfications() {
        log.info("Sending notifications");

        try(var scope = StructuredTaskScope.open(StructuredTaskScope.Joiner.awaitAllSuccessfulOrThrow())){
           scope.fork(this::sendUpcomingEventsNotification);
           scope.fork(this::sendTodayEventNotifications);
           scope.fork(this::sendBirthdayReminders);

           scope.join();

            messagingTemplate.convertAndSend("/topic/notifications-update", "REFRESH");
            log.info("Batch process complete. Single refresh signal sent to WebSockets.");
        }catch (RuntimeException e){
            log.error("Failed to process notifications: {}", e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Sends notifications for events occurring within the next 3 days.
     */
    public void sendUpcomingEventsNotification() {
        log.info("Sending upcoming events notification");
        LocalDate today = LocalDate.now();
        LocalDate threeDaysFromNow = LocalDate.now().plusDays(3);

        List<Events> upcomingEvents = eventsRepository
                .findEventsBetweenDates(today, threeDaysFromNow);

        for (Events event : upcomingEvents) {
            long daysUntil = java.time.temporal.ChronoUnit.DAYS.between(today, event.getEventDate());

            notificationService.createEventReminder(
                    event.getUser(),
                    event.getEventName(),
                    (int) daysUntil,
                    event.getEventDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            );
        }
    }

    /**
     * Sends notifications for events occurring today.
     */
    public void sendTodayEventNotifications() {
        log.info("Sending today events notification");
        LocalDate today = LocalDate.now();

        // Find events happening today
        List<Events> todayEvents = eventsRepository.findEventsByDate(today);

        for (Events event : todayEvents) {
            String eventTime = event.getEventTime() != null
                    ? event.getEventTime().toString()
                    : "לא צוין";

            notificationService.createEventTodayNotification(
                    event.getUser(),
                    event.getEventName(),
                    eventTime
            );
        }
    }

    /**
     * Sends birthday reminders for employees whose birthdays are within the next 7 days.
     */
    public void sendBirthdayReminders() {
        log.info("Sending birthday reminders notification");
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysFromNow = today.plusDays(7);

        // Find employees with birthdays in the next 7 days
        List<Employees> upcomingBirthdays = employeeRepository.findUpcomingBirthdays(today, sevenDaysFromNow);

        for (Employees employee : upcomingBirthdays) {
            notificationService.createBirthdayReminder(
                    employee.getUser(),
                    employee.getFirstName() + " " + employee.getLastName(),
                    employee.getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            );
        }
    }

}
