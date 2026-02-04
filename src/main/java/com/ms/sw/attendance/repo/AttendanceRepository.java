package com.ms.sw.attendance.repo;

import com.ms.sw.attendance.dto.AttendanceDto;
import com.ms.sw.attendance.dto.EmployeeOptionDto;
import com.ms.sw.attendance.model.Attendance;
import com.ms.sw.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {


    @Query("""
           select new com.ms.sw.attendance.dto.AttendanceDto(
                      a.id,
                      a.date,
                      concat(e.firstName, ' ', e.lastName),
                      a.employee.position,
                      a.employee.department,
                      a.checkInTime,
                      a.checkOutTime,
                      a.status                                                                            
                      )
           from Attendance a 
           join a.employee e
           where e.user.username = :username
           and a.date >= :startDate
           and a.date < :endDate
           and ( :department = '' or e.department = :department )
           and ( :employeeName = '' or concat(e.firstName,' ',e.lastName) = :employeeName )                                                             
           """)
    List<AttendanceDto> getAllRecords(
            @Param("username") String username,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("department") String department,
            @Param("employeeName") String employeeName);


    @Query("""
            select new com.ms.sw.attendance.dto.EmployeeOptionDto(
                concat(e.firstName, ' ', e.lastName),
                e.department
            )
            from Employees e
            where e.user.username = :username
            """)
    List<EmployeeOptionDto> loadMapOfEmployees(@Param("username") String username);
}
