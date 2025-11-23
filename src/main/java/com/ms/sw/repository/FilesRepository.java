package com.ms.sw.repository;

import com.ms.sw.entity.Documents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FilesRepository extends JpaRepository<Documents, Long> {

    @Query("""
           select d
           from Documents d 
           where d.employee.personalId = :personalId                   
                        """)
    List<Documents> getAllFilesByPersonalId(@Param("personalId") String personalId);

    @Modifying
    @Transactional
    @Query("""
            delete from Documents d 
            where d.filePath like concat('%', :fileName)
        """)
    void deleteByFileName(@Param("fileName") String fileName);
}
