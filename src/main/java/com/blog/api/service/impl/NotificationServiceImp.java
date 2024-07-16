package com.blog.api.service.impl;

import com.blog.api.dto.request.NotificationRequest;
import com.blog.api.dto.response.NotificationResponse;
import com.blog.api.dto.response.QuestionResponse;
import com.blog.api.entities.Article;
import com.blog.api.entities.Notification;
import com.blog.api.entities.Question;
import com.blog.api.entities.User;
import com.blog.api.exception.AppException;
import com.blog.api.exception.ErrorCode;
import com.blog.api.mapper.ArticleMapper;
import com.blog.api.mapper.NotificationMapper;
import com.blog.api.mapper.QuestionMapper;
import com.blog.api.mapper.UserMapper;
import com.blog.api.repository.ArticleRepository;
import com.blog.api.repository.NotificationRepository;
import com.blog.api.repository.QuestionRepository;
import com.blog.api.repository.UserRepository;
import com.blog.api.service.NotificationService;
import com.blog.api.types.TableType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class NotificationServiceImp implements NotificationService {

    NotificationRepository notificationRepository;
    UserRepository userRepository;
    UserMapper userMapper;
    NotificationMapper notificationMapper;
    ArticleRepository articleRepository;
    ArticleMapper articleMapper;
    QuestionRepository questionRepository;
    QuestionMapper questionMapper;


    @Override
    public List<NotificationResponse> getAll(int pageSize, int pageNumber) {

        List<NotificationResponse> notificationsResponse = new ArrayList<>();

        List<Notification> notifications = notificationRepository.findAll();
        log.info("NOTIFICATION " + notifications);

        notificationsResponse = notifications.stream().map((notification) -> {
            NotificationResponse notificationResponse = notificationMapper.toNotificationResponse(notification);
            notificationResponse.setSender(userMapper.toBasicUserResponse(notification.getSender()));
            notificationResponse.setReceiver(userMapper.toBasicUserResponse(notification.getReceiver()));
            return notificationResponse;
        }).toList();

        return notificationsResponse;
    }

    @Override
    public List<NotificationResponse> getAllByUser(String id , int pageNumber, int pageSize) {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        Sort sort = Sort.by("createdAt").descending();
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, sort);
        User user = userRepository.findByUsername(username).orElse(null);

        if(!user.getId().equals(id)) throw new AppException(ErrorCode.USER_CONFLICT);

        List<Notification> notifications = notificationRepository.findAllByReceiver(user, pageRequest);
        List<NotificationResponse> notificationResponses = notifications.stream().map((notification)-> {
            NotificationResponse notificationResponse = notificationMapper.toNotificationResponse(notification);
            User sender = userRepository.findById(notification.getSender().getId()).orElse(null);
            notificationResponse.setSender(userMapper.toBasicUserResponse(sender));
            notificationResponse.setReceiver(userMapper.toBasicUserResponse(user));

            if(notification.getContextType().equals(TableType.QUESTION)) {
                Question question = questionRepository.findById(notification.getContextId()).orElse(null);
                notificationResponse.setContext(question.getContent());
            }else if(notification.getContextType().equals(TableType.ARTICLE)) {
                Article article = articleRepository.findById(notification.getContextId()).orElse(null);
                notificationResponse.setContext(article.getTitle());
            }

            return notificationResponse;
        }).toList();


        return notificationResponses;
    }

    @Override
    public NotificationResponse create(NotificationRequest notificationRequest) {

        User sender = userRepository.findById(notificationRequest.getSender()).orElseThrow(() ->
                new AppException(ErrorCode.UNAUTHENTICATED));
        User receiver = userRepository.findById(notificationRequest.getReceiver()).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));

        if(sender.equals(receiver)) {
            throw new AppException(ErrorCode.USER_CONFLICT);
        }

        Notification notification = notificationMapper.toNotification(notificationRequest);
        notification.setSender(sender);
        notification.setReceiver(receiver);
        notification.setReaded(false);
        NotificationResponse notificationResponse =
                notificationMapper.toNotificationResponse(notificationRepository.save(notification));

        notificationResponse.setSender(userMapper.toBasicUserResponse(notification.getSender()));
        notificationResponse.setReceiver(userMapper.toBasicUserResponse(notification.getReceiver()));
        if(notification.getContextType().equals(TableType.QUESTION)) {
            Question question = questionRepository.findById(notification.getContextId()).orElse(null);
            notificationResponse.setContext(question.getContent());
        }else if(notification.getContextType().equals(TableType.ARTICLE)) {
            Article article = articleRepository.findById(notification.getContextId()).orElse(null);
            notificationResponse.setContext(article.getTitle());
        }
        return notificationResponse;
    }

    @Override
    public boolean read(String id) {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        User author = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        Notification notification = notificationRepository.findById(id).orElseThrow(() -> {
            return new AppException(ErrorCode.NOTIFICATION_NOT_FOUND);
        });

        if (author.equals(notification.getReceiver())) {
            System.out.println("SAME USER ");
            notification.setReaded(true);
            notificationRepository.save(notification);
            return true;

        } else {
            throw new AppException(ErrorCode.USER_CONFLICT);
        }
    }

    @Override
    public boolean readAll() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new AppException(ErrorCode.UNAUTHENTICATED)
        );

        List<Notification> unReadedNotifications = notificationRepository.findAllByReceiverAndIsReaded(user, false);

        unReadedNotifications = unReadedNotifications.stream().map((notification) -> {
            notification.setReaded(true);
            return notification;
        }).toList();

        notificationRepository.saveAll(unReadedNotifications);

        return true;
    }


}
