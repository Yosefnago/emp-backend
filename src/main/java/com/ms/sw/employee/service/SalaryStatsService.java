package com.ms.sw.employee.service;

import com.ms.sw.employee.dto.SalaryStatsDto;
import com.ms.sw.employee.repo.SalaryRepository;
import com.ms.sw.user.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalaryStatsService {

    private final SalaryRepository salaryRepository;

    public SalaryStatsService(SalaryRepository salaryRepository) {
        this.salaryRepository = salaryRepository;
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

}
