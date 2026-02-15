package com.ms.sw.user.repo;

import com.ms.sw.user.dto.ActivityLogsDto;
import com.ms.sw.user.model.ActivityLogs;
import com.ms.sw.views.dto.ActivityLogsListDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ActivityLogsRepository extends JpaRepository<ActivityLogs, Long> {


    @Query("""
        select
                a.fromUser,
                a.action,
                a.affectedEmployee,
                CAST( a .dateAction AS java.time.LocalDate),
                cast( a .timeAction as java.time.LocalTime)
                from ActivityLogs a
                where a.fromUser = :username
                order by a.dateAction desc , a.timeAction desc
        """)
    List<ActivityLogsDto> getLastActivityByUsername(@Param("username") String username, PageRequest pageRequest);


    @Query("""
        select
                a.fromUser,
                a.action,
                a.affectedEmployee,
                CAST( a .dateAction AS java.time.LocalDate),
                cast( a .timeAction as java.time.LocalTime)
                from ActivityLogs a
        where a.fromUser = :username
       """)
    List<ActivityLogsListDto> getAllActivity(@Param("username") String username);
}
