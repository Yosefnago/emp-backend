package com.ms.sw.employee.model;

import com.ms.sw.user.model.Department;
import com.ms.sw.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Table(name = "archived_employees")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArchivedEmployees {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "personal_id", unique = true)
    private String personalId;

    @Column(name = "gender")
    private String gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "family_status")
    private String familyStatus;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "position")
    private String position;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "job_type")
    private String jobType;

    @Column(name = "status")
    private String status;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Column(name = "archived_at")
    private LocalDate archivedAt;

    @Column(name = "archived_by")
    private String archivedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
