package com.ms.sw.user.model;

import com.ms.sw.employee.model.Employees;
import com.ms.sw.notifications.model.Notifications;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public  class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "email",unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "company_id")
    private String companyId;

    @Column(name = "company_address")
    private String companyAddress;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "last_login")
    private Timestamp lastLogin;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Events> events;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Notifications> notifications;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Department> departments;

}
