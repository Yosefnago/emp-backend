package com.ms.sw.repository;

import com.ms.sw.Dto.salaries.SalaryCalculationProjection;
import com.ms.sw.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;


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

    @Query("""
        SELECT s
        FROM Salary s
        JOIN s.employee e
        JOIN e.user u
        WHERE e.personalId = :personalId AND u.username = :username
        """)
    Optional<Salary> getSalaryDetailsByPersonalIdAndOwner(@Param("personalId") String personalId, @Param("username") String username);

    @Query("""
        SELECT new com.ms.sw.Dto.salaries.SalaryCalculationProjection(s.totalHoursMonth, s.salaryPerHour, CAST(s.bonus AS double)) 
        FROM Salary s
        JOIN s.employee e
        JOIN e.user u
        WHERE e.personalId = :personalId AND u.username = :username
    """)
    Optional<SalaryCalculationProjection> getSalaryCalculationDataByPersonalIdAndOwner(
            @Param("personalId") String personalId,
            @Param("username") String username
    );
}
