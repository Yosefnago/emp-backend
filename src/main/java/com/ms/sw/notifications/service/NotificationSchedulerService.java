package com.ms.sw.notifications.service;

import com.ms.sw.employee.model.Employees;
import com.ms.sw.employee.repo.EmployeeRepository;
import com.ms.sw.user.model.Events;
import com.ms.sw.user.repo.EventsRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service responsible for scheduling and sending automated notifications.
 *
 * <p>Uses Spring's {@link Scheduled} annotations to trigger tasks at specific times:
 * - Event reminders 3 days in advance
 * - Event notifications on the day of the event
 * - Birthday reminders 7 days before</p>
 */
@Service
public class NotificationSchedulerService {

    private final EventsRepository eventsRepository;

    private final EmployeeRepository employeeRepository;

    private final NotificationService notificationService;

    public NotificationSchedulerService(EventsRepository eventsRepository, EmployeeRepository employeeRepository, NotificationService notificationService) {
        this.eventsRepository = eventsRepository;
        this.employeeRepository = employeeRepository;
        this.notificationService = notificationService;
    }

    /**
     * Sends notifications for events occurring within the next 3 days.
     * Runs daily at 9:00 AM.
     */
    @Scheduled(cron = "0 0 9 * * ?")
    public void sendUpcomingEventsNotification() {
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
     * Runs daily at 8:00 AM.
     */
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendTodayEventNotifications() {
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
     * Runs daily at 7:00 AM.
     */
    @Scheduled(cron = "0 0 7 * * ?")
    public void sendBirthdayReminders() {
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
