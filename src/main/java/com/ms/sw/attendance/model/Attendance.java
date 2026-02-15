package com.ms.sw.attendance.model;

import com.ms.sw.employee.model.Employees;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "attendance")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "check_in_time")
    private LocalTime checkInTime;

    @Column(name = "check_out_time")
    private LocalTime checkOutTime;

    @Column(name = "total_hours")
    private double totalHours;

    @Column(name = "status")
    private String status;

    @Column(name = "travel_alow")
    private boolean travelAllow;

    @Column(name = "notes")
    private String notes;

    @Column(name = "is_attendance_closed",nullable = false,columnDefinition = "boolean default false")
    private boolean attendanceClosed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "personal_id",
            referencedColumnName = "personal_id",
            insertable = false,
            updatable = false
    )
    private Employees employee;
}
