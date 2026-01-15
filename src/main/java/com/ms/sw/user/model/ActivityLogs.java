package com.ms.sw.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private ActionType action;

    @Column(name = "affectedEmployee")
    private String affectedEmployee;

    @Column(name = "date_action")
    private LocalDate dateAction;

    @Column(name = "time_action")
    private LocalTime timeAction;
}
