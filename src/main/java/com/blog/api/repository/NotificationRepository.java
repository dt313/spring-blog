package com.blog.api.repository;

import com.blog.api.entities.Notification;
import com.blog.api.entities.User;
import com.blog.api.types.NotificationType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findAllByReceiverAndIsReaded(User receiver, boolean isReaded);
    List<Notification> findAllByReceiver(User receiver, PageRequest pageRequest);

    List<Notification> findAllBySenderAndReceiverAndTypeAndDirectObjectId
            (User sender, User receiver, NotificationType type, Long id);

    void deleteByDirectObjectId(Long directObjectId);

    // Xóa tất cả notifications của reply comment (reply có commentableId là id của comment cha)
    @Modifying
    @Transactional
    @Query("DELETE FROM Notification n WHERE n.directObjectId IN (SELECT c.id FROM Comment c WHERE c.commentableId = :commentId)")
    void deleteByRepliesOfComment(@Param("commentId") Long commentId);
}
