package com.ms.sw.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "activity_logs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "from_user")
    private String fromUser;

    @Column(name = "action")
    private String action;

    @Column(name = "date_action")
    private LocalDate dateAction;

    @Column(name = "time_action")
    private LocalTime timeAction;
}
