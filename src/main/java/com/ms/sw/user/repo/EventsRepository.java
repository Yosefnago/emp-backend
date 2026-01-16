package com.ms.sw.user.repo;

import com.ms.sw.user.dto.EventDto;
import com.ms.sw.user.model.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EventsRepository extends JpaRepository<Events, Long> {

    @Query("""
        select 
                e.eventName,
                e.eventDate,
                e.eventTime,
                e.priority,
                e.description,
                e.location,
                e.numberOfAttendance                                                
                 from Events e 
        where e.user.username = :username    
        order by e.eventDate,e.eventTime desc          
        """)
    List<EventDto> getEventsByUsername(@Param("username") String username);


    @Query("""
        select 
        e.eventName,
        e.eventDate,
        e.eventTime,
        e.priority,
        e.description,
        e.location,
        e.numberOfAttendance
          from Events  e
          where e.user.username = :username
          order by e.eventDate,e.eventTime desc                                                             
        """)
    List<EventDto> getUpcomingEvents(@Param("username") String username);


    @Query("""
        SELECT e FROM Events e
        WHERE e.eventDate BETWEEN :startDate AND :endDate
    """)
    List<Events> findEventsBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(""" 
            SELECT e FROM Events e 
            WHERE e.eventDate = :date
    """)
    List<Events> findEventsByDate(@Param("date") LocalDate date);
}
