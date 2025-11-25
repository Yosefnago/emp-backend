package com.ms.sw.repository;

import com.ms.sw.entity.Documents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FilesRepository extends JpaRepository<Documents, Long> {

    @Query("""
           select d
           from Documents d 
           join d.employee e
           join e.user u
           where e.personalId = :personalId AND u.username = :ownerUsername
           """)
    List<Documents> getAllFilesByPersonalIdAndOwner(@Param("personalId") String personalId, @Param("ownerUsername") String ownerUsername);

    @Query("""
           select d
           from Documents d
           join d.employee e
           join e.user u
           where d.id = :documentId AND u.username = :ownerUsername
           """)
    Optional<Documents> findByIdAndOwner(@Param("documentId") Long documentId, @Param("ownerUsername") String ownerUsername);

    @Modifying
    @Transactional
    @Query("""
            delete from Documents d 
            where d.id = :documentId
            and d.employee.user.username = :ownerUsername
        """)
    int deleteByIdAndOwner(@Param("documentId") Long documentId, @Param("ownerUsername") String ownerUsername);
}
