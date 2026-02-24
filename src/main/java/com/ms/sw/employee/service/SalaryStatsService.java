package com.ms.sw.employee.service;

import com.ms.sw.employee.dto.SalaryDetailsDto;
import com.ms.sw.employee.dto.SalaryStatsDto;
import com.ms.sw.employee.dto.SalaryUpdateDetailsRequestDto;
import com.ms.sw.employee.repo.SalaryDetailsRepository;
import com.ms.sw.employee.repo.SalaryRepository;
import com.ms.sw.user.model.User;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SalaryStatsService {

    private final SalaryRepository salaryRepository;
    private final SalaryDetailsRepository salaryDetailsRepository;

    public SalaryStatsService(SalaryRepository salaryRepository, SalaryDetailsRepository salaryDetailsRepository) {
        this.salaryRepository = salaryRepository;
        this.salaryDetailsRepository = salaryDetailsRepository;
    }

    public SalaryStatsDto getSalaryStats(User user, int year, int month){
        List<SalaryStatsDto> list = salaryRepository.getSalaryStats(user.getUsername(), year, month);

        if (list.isEmpty()) {
            return new SalaryStatsDto(0.0, 0.0, 0.0);
        }

        double total = list.stream()
                .mapToDouble(dto -> dto.totalSalary() != null ? dto.totalSalary() : 0.0)
                .sum();

        double avg = list.stream()
                .mapToDouble(dto -> dto.avgSalary() != null ? dto.avgSalary() : 0.0)
                .average()
                .orElse(0.0);

        double max = list.stream()
                .mapToDouble(dto -> dto.maxSalary() != null ? dto.maxSalary() : 0.0)
                .max()
                .orElse(0.0);

        System.out.println("Total salary: " + total);
        System.out.println("Avg salary: " + avg);
        System.out.println("Max salary: " + max);

        return new SalaryStatsDto(total,avg,max);
    }

    public SalaryDetailsDto getSalaryDetails(String username,String personalId){

        log.info("SalaryStatsService::getSalaryDetails invoked by {}",username);

        var dto = salaryDetailsRepository.findSalaryDetailsOfEmployee(username, personalId);
        return new SalaryDetailsDto(
                dto.pensionFund(),
                dto.providentFund(),
                dto.insuranceCompany(),
                dto.totalSeekDays(),
                dto.totalVacationDays(),
                dto.salaryPerHour(),
                dto.seniority(),
                dto.creditPoints()
        );
    }
    @Transactional
    public void updateSalaryDetails(String username, String personalId, SalaryUpdateDetailsRequestDto requestDto){
        log.info("SalaryStatsService::updateSalaryDetails invoked by {} for employee: {}",username,personalId);

        salaryDetailsRepository.updateSalaryDetailsForEmployee(
                username,
                personalId,
                requestDto.pensionFund(),
                requestDto.providentFund(),
                requestDto.insuranceCompany(),
                requestDto.salaryPerHour(),
                requestDto.seniority(),
                requestDto.creditPoints(),
                requestDto.totalSeekDays(),
                requestDto.totalVacationDays()
        );
    }


}
