package com.ms.sw.employee.repo;

import com.ms.sw.employee.dto.EmployeeDetailsResponse;
import com.ms.sw.employee.dto.EmployeeListResponse;
import com.ms.sw.employee.model.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employees, Long> {


    @Query("""
        SELECT e
        FROM Employees e
        JOIN e.user u
        WHERE u.username = :username
    """)
    List<EmployeeListResponse> getAllEmployeesByOwner(@Param("username") String username);

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
    Optional<EmployeeDetailsResponse> findByPersonalIdAndOwner(@Param("personalId") String personalId,
                                                               @Param("username") String username);

    Optional<Employees> findByPersonalId(String personalId);


    @Modifying
    @Query("""
    UPDATE Employees e SET 
        e.firstName = :firstName, 
        e.lastName = :lastName, 
        e.email = :email, 
        e.gender = :gender,
        e.birthDate = :birthDate,
        e.familyStatus = :familyStatus,
        e.phone = :phone,
        e.address = :address,
        e.city = :city,
        e.country = :country,
        e.position = :position,
        e.department = :department,
        e.hireDate = :hireDate,
        e.jobType = :jobType,
        e.status = :status
    WHERE e.personalId = :personalId AND e.user.username = :username
""")
    int updateEmployeeDetailsByPersonalIdAndOwner(
            @Param("personalId") String personalId,
            @Param("username") String username,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("email") String email,
            @Param("gender")  String gender,
            @Param("birthDate")  LocalDate birthDate,
            @Param("familyStatus")   String familyStatus,
            @Param("phone") String  phone,
            @Param("address")  String  address,
            @Param("city") String  city,
            @Param("country")  String  country,
            @Param("position") String position,
            @Param("department") String department,
            @Param("hireDate") LocalDate hireDate,
            @Param("jobType") String  jobType,
            @Param("status") String status
    );

    @Query("""
    select count(e)
        from Employees e
        where e.status = 'present'
        and e.user.username = :username
    """)
    int countEmployeeByStatus(@Param("username") String username);


    @Query(value = """
    SELECT * FROM employees
    WHERE 
        (EXTRACT(MONTH FROM CAST(birth_date AS DATE)) = EXTRACT(MONTH FROM CAST(:startDate AS DATE))
         AND EXTRACT(DAY FROM CAST(birth_date AS DATE)) >= EXTRACT(DAY FROM CAST(:startDate AS DATE)))
    OR
        (EXTRACT(MONTH FROM CAST(birth_date AS DATE)) = EXTRACT(MONTH FROM CAST(:endDate AS DATE))
         AND EXTRACT(DAY FROM CAST(birth_date AS DATE)) <= EXTRACT(DAY FROM CAST(:endDate AS DATE)))
    OR
        (EXTRACT(MONTH FROM CAST(birth_date AS DATE)) > EXTRACT(MONTH FROM CAST(:startDate AS DATE))
         AND EXTRACT(MONTH FROM CAST(birth_date AS DATE)) < EXTRACT(MONTH FROM CAST(:endDate AS DATE)))
""", nativeQuery = true)
    List<Employees> findUpcomingBirthdays(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}