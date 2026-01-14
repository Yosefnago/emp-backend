package com.ms.sw.employee.repo;

import com.ms.sw.employee.model.ArchivedEmployees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArchivedEmployeeRepository extends JpaRepository<ArchivedEmployees, Long> {

    /**
     * Find archived employee by personal ID
     */
    Optional<ArchivedEmployees> findByPersonalId(String personalId);

    /**
     * Find all archived employees by the employee ID (useful for tracking same person)
     */
    List<ArchivedEmployees> findByEmployeeId(Long employeeId);

    /**
     * Find archived employees by department
     */
    List<ArchivedEmployees> findByDepartment(String department);

    /**
     * Find archived employees by who archived them (for audit trail)
     */
    List<ArchivedEmployees> findByArchivedBy(String archivedBy);

    /**
     * Find archived employees by status
     */
    List<ArchivedEmployees> findByStatus(String status);

    /**
     * Find archived employees with custom query to search by name
     */
    @Query("""
        SELECT ae FROM ArchivedEmployees ae
        WHERE LOWER(CONCAT(ae.firstName, ' ', ae.lastName)) LIKE LOWER(CONCAT('%', :searchName, '%'))
    """)
    List<ArchivedEmployees> searchByName(@Param("searchName") String searchName);
}


