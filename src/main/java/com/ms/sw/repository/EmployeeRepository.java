package com.ms.sw.repository;

import com.ms.sw.entity.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employees, Long> {


    @Query("""
        SELECT e
        FROM Employees e
        JOIN e.user u
        WHERE u.username = :username
    """)
    List<Employees> getAllEmployeesByOwner(@Param("username") String username);

    @Query("""
        SELECT COUNT(e)
        FROM Employees e
        JOIN e.user u
        WHERE u.username = :username
    """)
    int countEmployeesByUsername(@Param("username") String username);

    void deleteEmployeesByPersonalId(String id);


    @Query("""
        SELECT e 
        FROM Employees e
        JOIN e.user u
        WHERE e.personalId = :personalId AND u.username = :username
    """)
    Optional<Employees> findByPersonalIdAndOwner(@Param("personalId") String personalId,
                                                 @Param("username") String username);

    @Query("""
       select e
       from Employees e
       where e.personalId = :personalId
       """)
    Employees getEmployeesByPersonalId(String personalId);
}