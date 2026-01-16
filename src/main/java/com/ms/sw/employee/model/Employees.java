package com.ms.sw.employee.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ms.sw.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employees {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "personal_id",unique = true)
    private String personalId;

    @Column(name = "gender")
    private String gender;

    @Column(name = "birth_date")
    private Timestamp birthDate;

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

    @Column(name = "department")
    private String department;

    @Column(name = "hire_date")
    private Timestamp hireDate;

    @Column(name = "job_type")
    private String jobType;

    @Column(name = "status")
    private String status;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "status_attendance")
    private String statusAttendance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_username", referencedColumnName = "username")
    @JsonIgnore
    private User user;


}
