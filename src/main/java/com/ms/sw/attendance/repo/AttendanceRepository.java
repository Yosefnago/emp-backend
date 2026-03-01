package com.ms.sw.attendance.repo;

import com.ms.sw.attendance.dto.AttendanceDto;
import com.ms.sw.attendance.dto.EmployeeOptionDto;
import com.ms.sw.attendance.model.Attendance;
import com.ms.sw.attendance.dto.AttendancePayrollDto;
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
                      a.notes,
                      a.attendanceClosed
                      )
           from Attendance a
           join a.employee e
           where e.user.username = :username
           and e.status = 'ACTIVE'
           and a.date >= :startDate
           and a.date < :endDate
           and ( :department = '' or e.department.departmentName = :department )
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
                e.personalId,
                concat(e.firstName, ' ', e.lastName),
                e.department.departmentName
            )
            from Employees e
            where e.user.username = :username
            and e.status = 'ACTIVE'
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

    @Query("""
    SELECT new com.ms.sw.attendance.dto.AttendancePayrollDto(
            a.employee.personalId,
            a.date,
            a.totalHours,
            a.status,
            a.travelAllow
    )
    FROM Attendance a
    WHERE a.employee.user.username = :username
      AND a.employee.personalId = :personalId
      and a.employee.status = 'ACTIVE'
      AND YEAR(a.date) = :year
      AND MONTH(a.date) = :month
    """)
    List<AttendancePayrollDto> loadAttendancePayrollDto(
            @Param("username") String username,
            @Param("personalId") String personalId,
            @Param("year") int year,
            @Param("month") int month
            );

    @Modifying
    @Query("""
        update Attendance a
        set a.attendanceClosed = true
        where a.employee.user.username = :username
        AND a.employee.personalId = :personalId
        and YEAR(a.date) = :year
        and MONTH(a.date) = :month
    
    """)
    void updateAttendanceToClose(
            @Param("username") String username,
            @Param("personalId") String personalId,
            @Param("year") int year,
            @Param("month") int month
    );

    @Query("""
        SELECT a FROM Attendance a
        WHERE a.employee.personalId = :personalId
        AND a.employee.user.username = :username
        AND EXTRACT(MONTH FROM a.date) = :month
        AND EXTRACT(YEAR FROM a.date) = :year
        AND a.date <= CURRENT_DATE
    """)
    List<Attendance> findCurrentMonthAttendanceUpToToday(
            @Param("username") String username,
            @Param("personalId") String personalId,
            @Param("month") int month,
            @Param("year") int year
    );

    @Modifying
    @Query("DELETE FROM Attendance a WHERE a.employee.id = :employeeId")
    void deleteByEmployeeId(@Param("employeeId") Long employeeId);
}
