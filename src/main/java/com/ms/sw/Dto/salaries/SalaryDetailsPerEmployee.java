package com.ms.sw.Dto.salaries;


import com.dev.tools.Markers.DtoMarker;

/**
 * {@code SalaryDetailsPerEmployee} is an immutable Data Transfer Object (DTO)
 * designed to convey the complete and current financial and payroll configuration
 * details for a single employee.
 * <p>This record serves as the explicit API contract for endpoints loading detailed
 * salary information (e.g., {@code GET /salary/emp/{personalId}}).
 * @see com.ms.sw.controller.SalaryController
 */
@DtoMarker
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
