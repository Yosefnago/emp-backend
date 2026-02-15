package com.ms.sw.employee.model;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "salary_details")
@Entity
@Data
public class SalaryDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_id", referencedColumnName = "personal_id")
    private Employees employee;

    @Column(name = "total_seek_days")
    private double totalSeekDays;

    @Column(name = "total_vacation_days")
    private double totalVacationDays;

    @Column(name = "salary_per_hour")
    private double salaryPerHour;

    @Column(name = "seniority")
    private double seniority;

    @Column(name = "credit_points")
    private double creditPoints;

}
