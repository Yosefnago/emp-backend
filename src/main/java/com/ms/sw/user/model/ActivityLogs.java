package com.ms.sw.user.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "activity_logs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ActivityLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

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
