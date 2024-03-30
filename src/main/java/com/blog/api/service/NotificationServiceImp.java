package com.blog.api.service;

import com.blog.api.exception.NotFoundException;
import com.blog.api.models.dto.NotificationDTO;
import com.blog.api.models.entity.Article;
import com.blog.api.models.entity.Notification;
import com.blog.api.models.entity.User;
import com.blog.api.models.mapper.NotificationMapper;
import com.blog.api.models.request.NotificationRequest;
import com.blog.api.repository.ArticleRepository;
import com.blog.api.repository.NotificationRepository;
import com.blog.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class NotificationServiceImp implements NotificationService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public List<NotificationDTO> getALlNotifications() {
        List<Notification> notiList = notificationRepository.findAll();
        List<NotificationDTO> result = new ArrayList<>();
        for (Notification notification : notiList){
            result.add(NotificationMapper.toNotificationDTO(notification));
        }
        System.out.print("Length Of Notification : " + result.size());
        return result;

    }
    @Override
    public List<NotificationDTO> getNotificationByTarget(Long id) {
         User isExist = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User is not exist : id = " + id));
        List<NotificationDTO> result = new ArrayList<>();

        List<Notification> list = notificationRepository.findNotificationByTarget(isExist);
        if (list.size() > 0) {
            for (Notification item : list) {
                result.add(NotificationMapper.toNotificationDTO(item));
            }
        }
        return result;

    }

    @Override
    public List<NotificationDTO> getNotificationBySender(Long id) {
        User isExist = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User is not exist : id = " + id));
        List<NotificationDTO> result = new ArrayList<>();

        List<Notification> list = notificationRepository.findNotificationBySender(isExist);
        if (list.size() > 0) {
            for (Notification item : list) {
                result.add(NotificationMapper.toNotificationDTO(item));
            }
        }
        return result;
    }

    @Override
    public NotificationDTO createNotification(NotificationRequest notificationRequest) {
        if(notificationRequest.getSender().equals(notificationRequest.getTarget())) {
            throw new NotFoundException("Sender is same with target id  = " + notificationRequest.getSender());
        }
        User sender = userRepository.findById(notificationRequest.getSender()).orElseThrow(() -> new NotFoundException("Sender is not exist : id = " + notificationRequest.getSender()));
        User target = userRepository.findById(notificationRequest.getTarget()).orElseThrow(() -> new NotFoundException("Sender is not exist : id = " + notificationRequest.getTarget()));
        Article context = articleRepository.findById(notificationRequest.getContext()).orElseThrow(() -> new NotFoundException("Context is not exist : id = " + notificationRequest.getContext()));

        Notification notification = new Notification(notificationRequest.getType(), notificationRequest.getAction(), sender, context, target);
        NotificationDTO result = NotificationMapper.toNotificationDTO(notificationRepository.save(notification));
        return result;
    }

    @Override
    public NotificationDTO getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new NotFoundException("Notification Not Found with Id : " + id));
        return NotificationMapper.toNotificationDTO(notification);
    }



    @Override
    public NotificationDTO readNotificationById(Long id) {
        Notification isExist = notificationRepository.findById(id).map((notification) -> {
            notification.setRead(true);
            return notificationRepository.save(notification);
        }).orElseGet(() -> {
            throw new NotFoundException("Notification Not Found with Id : " + id);
        });

        return NotificationMapper.toNotificationDTO(isExist);
    }

    @Override
    public List<NotificationDTO> readAllNotificationByTarget(Long id) {
        User target = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Sender is not exist : id = " + id));
        List<Notification> unReadedList = notificationRepository.findNotificationByTargetAndIsRead(target, false);
        List<NotificationDTO> result = new ArrayList<>();
        if(unReadedList.size() > 0) {
            for(Notification unread : unReadedList) {
                unread.setRead(true);
                result.add(NotificationMapper.toNotificationDTO(notificationRepository.save(unread)));
            }
        }
        return result;
    }

    @Override
    public boolean deleteNotificationById(Long id) {
        boolean isExist = notificationRepository.existsById(id);
        if(isExist) {
            notificationRepository.deleteById(id);
            return true;
        }else {
            throw new NotFoundException("Notification Not Found with Id : " + id);
        }
    }
}
