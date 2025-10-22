package com.ms.sw.repository;

import com.ms.sw.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    @Query("""
        select 
        count(e)
        from Employees e 
        where e.user.username = :username     
        """)
    int loadNumberOfEmployeesByUsername(String username);
}
