package com.ms.sw.Dto.salaries;

public record SalaryDetailsPerEmployee(
        double salaryPerHour,      // שכר שעה
        double monthlySalary,      // שכר חודשי
        String bonus,              // בונוס
        String bankName,           // שם בנק
        String bankAccount,        // מספר חשבון בנק
        String pensionFund,        // קרן פנסיה
        double totalHoursMonth,    // סה"כ שעות בחודש
        double overtimeHours,      // שעות נוספות
        double vacationDays        // ימי חופשה
) {}
