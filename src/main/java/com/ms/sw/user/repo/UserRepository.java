package com.ms.sw.user.repo;

import com.ms.sw.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);


    @Modifying
    @Query("""
        update User u set 
                u.email = :email,
                u.companyId = :companyId,
                u.companyAddress = :companyAddress,
                u.phoneNumber =:phoneNumber
                where u.username = :username                                 
        """)
    void updateUserProfileByUsername(
            @Param("username") String username,
            @Param("email") String email,
            @Param("companyId") String companyId,
            @Param("companyName") String companyName,
            @Param("companyAddress")  String companyAddress,
            @Param("phoneNumber") String phoneNumber

    );
}
