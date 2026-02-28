package com.ms.sw.user.repo;

import com.ms.sw.user.dto.DepartmentDetailsDto;
import com.ms.sw.user.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long> {

    Optional<Department> findByDepartmentName(String departmentName);


    @Query("""
    SELECT d 
    FROM Department d 
    LEFT JOIN FETCH d.employeesList 
    WHERE d.departmentName = :departmentName 
    AND d.user.username = :username
    """)
    Optional<Department> findDepartmentWithEmployees(
            @Param("username") String username,
            @Param("departmentName") String departmentName);
}
