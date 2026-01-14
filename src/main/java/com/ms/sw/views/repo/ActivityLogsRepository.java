package com.ms.sw.repository;

import com.ms.sw.Dto.ActivityLogsDto;
import com.ms.sw.entity.ActivityLogs;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ActivityLogsRepository extends JpaRepository<ActivityLogs, String> {


    @Query("""
        select a.fromUser,a.action,a .dateAction,a .timeAction
                from ActivityLogs as a  
                where a.fromUser = :username       
                order by a.dateAction desc , a.timeAction desc 
                """)
    List<ActivityLogsDto> getLastActivityByUsername(@Param("username") String username, PageRequest pageRequest);
}
