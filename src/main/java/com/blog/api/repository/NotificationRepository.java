package com.blog.api.repository;

import com.blog.api.entities.Notification;
import com.blog.api.entities.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,String> {
    List<Notification> findAllByReceiverAndIsReaded(User receiver, boolean isReaded);
    List<Notification> findAllByReceiver(User receiver, PageRequest pageRequest);
}
