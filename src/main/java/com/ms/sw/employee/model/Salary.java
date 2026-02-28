package com.ms.sw.employee.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "salary")
@Data
public class Salary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employees employee;

    @Column(name = "salary_amount")
    private double salaryAmount;

    @Column(name = "salary_month")
    private int salaryMonth;

    @Column(name = "salary_year")
    private int salaryYear;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "path_of_tlush")
    private String pathOfTlush;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());
        if (paymentDate == null) {
            paymentDate = LocalDate.now();
        }
    }
}
