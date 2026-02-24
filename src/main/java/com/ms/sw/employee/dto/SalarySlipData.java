package com.ms.sw.employee.dto;

import java.time.LocalDate;

public record SalarySlipData(
        // Company info
        String companyName,
        String companyId,
        String companyAddress,

        // Period
        int year,
        int month,

        // Employee info
        String employeeName,
        String personalId,
        String department,
        LocalDate paymentDate,
        String pensionFund,
        String providentFund,
        String insuranceCompany,

        // Work hours
        double  regularHours,
        double  overtime125Hours,
        double  overtime150Hours,
        double  travelDays,

        // Pay rates and amounts
        double  hourlyRate,
        double  regularPay,
        double  overtime125Pay,
        double  overtime150Pay,
        double  travelAllowance,
        double  grossSalary,

        // Deductions
        double  employeePension,
        double  nationalInsurance,
        double  taxableIncome,
        double  incomeTax,
        double  totalDeductions,
        double  netSalary,

        // Tax info
        double  creditPoints,

        // Employer costs
        double  employerPension,
        double  employerSeverance,
        double  employerNI,
        double  totalEmployerCost
) {
}
