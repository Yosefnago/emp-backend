package com.ms.sw.repository;

import com.ms.sw.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface SalaryRepository extends JpaRepository<Salary, Long> {

    @Query("""
        SELECT SUM(s.monthlySalary)
        FROM Salary s
        JOIN s.employee e
        JOIN e.user u
        WHERE u.username = :username
    """)
    BigDecimal getSalariesByOwner(@Param("username") String username);

    @Query("""
        SELECT s.totalHoursMonth
        FROM Salary s
        JOIN s.employee e
        WHERE e.personalId = :personalId
    """)
    double getTotalHoursMonthByPersonalId(@Param("personalId") String personalId);

    @Query("""
        SELECT s.salaryPerHour
        FROM Salary s
        JOIN s.employee e
        WHERE e.personalId = :personalId
    """)
    double getSalaryPerHourByPersonalId(@Param("personalId") String personalId);

    @Query("""
        SELECT s.bonus
        FROM Salary s
        JOIN s.employee e
        WHERE e.personalId = :personalId
    """)
    Double getBonusByPersonalId(@Param("personalId") String personalId);

}
