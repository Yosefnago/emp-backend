package com.ms.sw.employee.repo;

import com.ms.sw.employee.model.ArchivedEmployees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchivedEmployeeRepository extends JpaRepository<ArchivedEmployees, Long> {
}


