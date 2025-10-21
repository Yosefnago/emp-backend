package com.ms.sw.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

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

    @Column(name = "last_login")
    private Timestamp lastLogin;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;




}
