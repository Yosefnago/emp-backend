package com.ms.sw.employee.repo;

import com.ms.sw.employee.dto.SalaryStatsDto;
import com.ms.sw.employee.model.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Long> {

    @Query("""
        select new com.ms.sw.employee.dto.SalaryStatsDto(
             sum (s.salaryAmount),
             avg (s.salaryAmount),
             max (s.salaryAmount)      
            )
        from Salary s 
        where s.employee.user.username  = :username
        AND s.salaryYear = :year
        AND s.salaryMonth = :month              
    """)
    List<SalaryStatsDto> getSalaryStats(@Param("username") String username, int year, int month);

}
