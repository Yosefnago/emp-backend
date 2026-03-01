package com.ms.sw.notifications.util;


public class NotificationMessages {

    // Event notifications
    public static final String EVENT_UPCOMING_REMINDER = "תזכורת: האירוע '%s' יתקיים בעוד %d ימים (%s)";
    public static final String EVENT_TODAY = "האירוע '%s' מתקיים היום בשעה %s";

    // Birthday notifications
    public static final String BIRTHDAY_REMINDER = "יום הולדת לעובד/ת %s מגיע ב-%s";

    public static final String PAYROLL_NOTIFY = "נוצר תלוש שכר לעובד %s";
    private NotificationMessages() {
        // Prevent instantiation
    }
}