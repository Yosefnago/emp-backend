package com.ms.sw.employee.repo;

import com.ms.sw.employee.dto.SalaryDetailsDto;
import com.ms.sw.employee.model.SalaryDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryDetailsRepository extends JpaRepository<SalaryDetails, Long> {

    @Query("""
        select new com.ms.sw.employee.dto.SalaryDetailsDto(
                s.pensionFund,
                s.providentFund,
                s.insuranceCompany,
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


    @Modifying
    @Query("""
        update SalaryDetails s
        set
        s.pensionFund = :pensionFund,
        s.providentFund = :providentFund,
        s.insuranceCompany = :insuranceCompany,
        s.salaryPerHour = :salaryPerHour,
        s.seniority = :seniority,
        s.creditPoints = :creditPoints,
        s.totalSeekDays = :totalSeekDays,
        s.totalVacationDays = :totalVacationDays
        where s.employee.user.username = :username and s.employee.personalId = :personalId       
        """)
    void updateSalaryDetailsForEmployee(
            @Param("username") String username,
            @Param("personalId") String personalId,
            @Param("pensionFund") String pensionFund,
            @Param("providentFund") String providentFund,
            @Param("insuranceCompany") String insuranceCompany,
            @Param("salaryPerHour") double salaryPerHour,
            @Param("seniority")  double seniority,
            @Param("creditPoints")  double creditPoints,
            @Param("totalSeekDays")   double totalSeekDays,
            @Param("totalVacationDays") double totalVacationDays
    );


}
