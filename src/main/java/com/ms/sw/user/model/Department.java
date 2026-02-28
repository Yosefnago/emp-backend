package com.ms.sw.user.model;

import com.ms.sw.employee.model.Employees;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "departments")
@Data
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_of_create")
    private LocalDate dateOfCreate;

    @Column(name = "annual_placement")
    private long annualPlacement;

    @Column(name = "department_name")
    private String departmentName;

    @Column(name = "department_code")
    private String departmentCode;

    @Column(name = "department_manager")
    private String departmentManager;

    @Column(name = "department_phone")
    private String departmentPhone;

    @Column(name = "department_mail")
    private String departmentMail;

    @OneToMany(mappedBy = "department")
    @ToString.Exclude
    private List<Employees> employeesList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
