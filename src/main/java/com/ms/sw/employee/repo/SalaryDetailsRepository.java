package com.ms.sw.employee.repo;

import com.ms.sw.employee.dto.SalaryDetailsDto;
import com.ms.sw.employee.model.SalaryDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryDetailsRepository extends JpaRepository<SalaryDetails, Long> {

    @Query("""
        select new com.ms.sw.employee.dto.SalaryDetailsDto(
                s.totalSeekDays,
                s.totalVacationDays,
                s.salaryPerHour,
                s.seniority   ,
                s.creditPoints
                )
        from SalaryDetails s
        where s.employee.user.username =:username
        AND s.employee.personalId = :personalId
        """)
    SalaryDetailsDto findSalaryDetailsOfEmployee(@Param("username") String username,@Param("personalId") String personalId);


}
