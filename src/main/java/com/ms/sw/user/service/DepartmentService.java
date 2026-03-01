package com.ms.sw.user.service;

import com.ms.sw.user.dto.DepartmentDetailsDto;
import com.ms.sw.user.dto.EmployeeSummaryDto;
import com.ms.sw.user.model.Department;
import com.ms.sw.user.repo.DepartmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public DepartmentDetailsDto getDepartmentDetails(String username,String departmentName){

        Department d = departmentRepository.findDepartmentWithEmployees(username,departmentName)
                .orElseThrow(()->new RuntimeException("Department with username " + username + " does not exist"));

        List<EmployeeSummaryDto> employeeDtos =
                d.getEmployeesList().stream()
                        .map(e -> new EmployeeSummaryDto(
                                e.getId(),
                                e.getFirstName(),
                                e.getLastName(),
                                e.getPosition(),
                                e.getEmail()
                        ))
                        .toList();

        return new DepartmentDetailsDto(
                d.getDateOfCreate(),
                d.getAnnualPlacement(),
                d.getDepartmentCode(),
                d.getDepartmentManager(),
                d.getDepartmentPhone(),
                d.getDepartmentMail(),
                employeeDtos
        );
    }
}
