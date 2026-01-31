package com.ms.sw.notifications.repo;

import com.ms.sw.notifications.model.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, Long> {


    @Query("""
        SELECT n FROM Notifications n
        JOIN n.user u
        WHERE u.username = :username
        ORDER BY n.createdAt DESC
    """)
    List<Notifications> findAllByUsername(@Param("username") String username);


    @Query("""
        SELECT COUNT(n) FROM Notifications n
        JOIN n.user u
        WHERE u.username = :username
        AND n.isRead = false
    """)
    int countUnreadByUsername(@Param("username") String username);


    @Modifying
    @Query("""
        UPDATE Notifications n
        SET n.isRead = true
        WHERE n.id = :id
        AND n.user.username = :username
    """)
    int markAsReadByIdAndUsername(@Param("id") Long id, @Param("username") String username);


    @Modifying
    @Query("""
        UPDATE Notifications n
        SET n.isRead = true
        WHERE n.user.username = :username
        AND n.isRead = false
    """)
    void markAllAsReadByUsername(@Param("username") String username);


    @Modifying
    @Query("""
        DELETE FROM Notifications n
        WHERE n.id = :id
        AND n.user.username = :username
    """)
    int deleteByIdAndUsername(@Param("id") Long id, @Param("username") String username);


    @Modifying
    @Query("""
        DELETE FROM Notifications n
        WHERE n.user.username = :username
    """)
    void deleteAllByUsername(@Param("username") String username);
}