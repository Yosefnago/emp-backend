package com.ms.sw.attendance.repo;

import com.ms.sw.attendance.dto.AttendanceDto;
import com.ms.sw.attendance.dto.EmployeeOptionDto;
import com.ms.sw.attendance.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {


    @Query("""
           select new com.ms.sw.attendance.dto.AttendanceDto(
                      a.employee.personalId,
                      a.date,
                      concat(e.firstName, ' ', e.lastName),
                      a.checkInTime,
                      a.checkOutTime,
                      a.status,
                      a.travelAllow,          
                      a.notes\s
                      )
           from Attendance a\s
           join a.employee e
           where e.user.username = :username
           and a.date >= :startDate
           and a.date < :endDate
           and ( :department = '' or e.department = :department )
           and ( :employeeName = '' or concat(e.firstName,' ',e.lastName) = :employeeName )                                                            \s
          \s""")
    List<AttendanceDto> getAllRecords(
            @Param("username") String username,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("department") String department,
            @Param("employeeName") String employeeName);


    @Query("""
            select new com.ms.sw.attendance.dto.EmployeeOptionDto(
                e.personalId,        
                concat(e.firstName, ' ', e.lastName),
                e.department
            )
            from Employees e
            where e.user.username = :username
            """)
    List<EmployeeOptionDto> loadMapOfEmployees(@Param("username") String username);

    @Modifying
    @Query("""
            UPDATE Attendance a
            SET 
                a.checkInTime  = :checkInTime,
                a.checkOutTime = :checkOutTime,
                a.status       = :status,
                a.notes = :notes        
            WHERE a.employee.personalId = :personalId
            and a.date = :date       
            AND a.employee.user.username = :username
        """)
    void updateOwnedRecord(
            @Param("personalId") String personalId,
            @Param("username") String username,
            @Param("date") LocalDate date,
            @Param("checkInTime") LocalTime checkInTime,
            @Param("checkOutTime") LocalTime checkOutTime,
            @Param("status") String status,
            @Param("notes") String notes
            );
}
