package com.ms.sw.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "salary")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Salary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @JsonBackReference
    private Employees employee;


    @Column(name = "salary_per_hour")
    private double salaryPerHour;

    @Column(name = "monthly_salary")
    private double monthlySalary;

    @Column(name = "bonus")
    private String bonus;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_account")
    private String bankAccount;

    @Column(name = "pension_fund")
    private String pensionFund;

    @Column(name = "total_hours_month")
    private double totalHoursMonth;

    @Column(name = "overtime_hours")
    private double overtimeHours;

    @Column(name = "vacation_days")
    private double vacationDays;

    @Column(name = "month_date")
    private String monthDate;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

}
