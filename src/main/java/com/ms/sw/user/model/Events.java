package com.ms.sw.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Table(name = "events")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Events {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "event_date")
    private LocalDate eventDate;

    @Column(name = "event_time")
    private LocalTime eventTime;

    @Column(name = "priority")
    private String priority;

    @Column(name = "description")
    private String description;

    @Column(name = "location")
    private String location;

    @Column(name = "number_of_attendance")
    private Integer numberOfAttendance;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
