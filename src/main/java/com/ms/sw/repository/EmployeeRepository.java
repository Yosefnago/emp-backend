package com.ms.sw.repository;

import com.ms.sw.entity.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Modifying
    @Query("""
         DELETE FROM Employees e 
         WHERE e.personalId = :personalId 
         AND e.user.username = :ownerUsername
                  """)
    int deleteByPersonalIdAndOwner(@Param("personalId") String personalId, @Param("ownerUsername") String ownerUsername);


    @Query("""
        SELECT e 
        FROM Employees e
        JOIN e.user u
        WHERE e.personalId = :personalId AND u.username = :username
    """)
    Optional<Employees> findByPersonalIdAndOwner(@Param("personalId") String personalId,
                                                 @Param("username") String username);

    Optional<Employees> findByPersonalId(String personalId);


    @Modifying
    @Query("""
    UPDATE Employees e SET 
        e.firstName = :firstName, 
        e.lastName = :lastName, 
        e.email = :email, 
        e.position = :position,
        e.department = :department,
        e.status = :status
    WHERE e.personalId = :personalId AND e.user.username = :username
""")
    int updateEmployeeDetailsByPersonalIdAndOwner(
            @Param("personalId") String personalId,
            @Param("username") String username,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("email") String email,
            @Param("position") String position,
            @Param("department") String department,
            @Param("status") String status
    );
}