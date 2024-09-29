package com.blog.api.service.impl;

import com.blog.api.dto.request.NotificationRequest;
import com.blog.api.dto.response.CommentDirect;
import com.blog.api.dto.response.NotificationResponse;
import com.blog.api.entities.Article;
import com.blog.api.entities.Comment;
import com.blog.api.entities.Notification;
import com.blog.api.entities.User;
import com.blog.api.exception.AppException;
import com.blog.api.exception.ErrorCode;
import com.blog.api.mapper.ArticleMapper;
import com.blog.api.mapper.NotificationMapper;
import com.blog.api.mapper.UserMapper;
import com.blog.api.repository.ArticleRepository;
import com.blog.api.repository.CommentRepository;
import com.blog.api.repository.NotificationRepository;
import com.blog.api.repository.UserRepository;
import com.blog.api.service.NotificationService;
import com.blog.api.types.NotificationType;
import com.blog.api.types.TableType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
    CommentRepository commentRepository;


    @Override
    public List<NotificationResponse> getAll(int pageSize, int pageNumber) {

        List<NotificationResponse> notificationsResponse = new ArrayList<>();

        List<Notification> notifications = notificationRepository.findAll();
        notificationsResponse = notifications.stream().map((notification) -> {
            NotificationResponse notificationResponse = notificationMapper.toNotificationResponse(notification);
            notificationResponse.setSender(userMapper.toBasicUserResponse(notification.getSender()));
            notificationResponse.setReceiver(userMapper.toBasicUserResponse(notification.getReceiver()));
            return notificationResponse;
        }).toList();

        return notificationsResponse;
    }

    @Override
    public List<NotificationResponse> getAllByUser(Long id, int pageNumber, int pageSize) {

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElse(null);

        Sort sort = Sort.by("createdAt").descending();
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, sort);

        if (!user.getId().equals(id)) {
            throw new AppException(ErrorCode.USER_CONFLICT);
        }

        List<Notification> notifications = notificationRepository.findAllByReceiver(user, pageRequest);

        List<NotificationResponse> notificationResponses = notifications.stream().map((notification)-> {
            NotificationResponse notificationResponse = notificationMapper.toNotificationResponse(notification);
            User sender = userRepository.findById(notification.getSender().getId()).orElse(null);
            notificationResponse.setSender(userMapper.toBasicUserResponse(sender));
            notificationResponse.setReceiver(userMapper.toBasicUserResponse(user));

            if (notification.getContextType().equals(TableType.ARTICLE)) {
                Article article = articleRepository.findById(notification.getContextId()).orElse(null);
                notificationResponse.setContext(articleMapper.toArticleResponse(article));
            }

            if (notification.getDirectObjectType().equals(TableType.COMMENT)) {
                Comment comment = commentRepository.findById(notification.getDirectObjectId()).orElse(null);

                if(Objects.nonNull(comment) && comment.getCommentType().equals(TableType.COMMENT)) {
                    CommentDirect commentDirect =
                            CommentDirect.builder().direct_id(comment.getId()).parentId(comment.getCommentableId()).build();
                        notificationResponse.setDirectObject(commentDirect);
                }

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

        // one day for reaction
        if (notificationRequest.getType().equals(NotificationType.REACT_ARTICLE)
                || notificationRequest.getType().equals(NotificationType.REACT_COMMENT)
        ) {

            List<Notification> exists = notificationRepository.findAllBySenderAndReceiverAndTypeAndDirectObjectId
                    (sender, receiver, notificationRequest.getType(), notificationRequest.getDirectObjectId());

            if (!exists.isEmpty()) {
                Date oneDayAgo = Date.from(Instant.now().minus(1, ChronoUnit.MINUTES));
                exists.sort(Comparator.comparing(Notification::getCreatedAt).reversed());
                Date createdAt = Date.from(exists.get(0).getCreatedAt());

                if (createdAt.before(new Date()) && createdAt.after(oneDayAgo)) {
                    return null;
                }
            }
        }

        Notification notification = notificationMapper.toNotification(notificationRequest);
        notification.setSender(sender);
        notification.setReceiver(receiver);
        NotificationResponse notificationResponse =
                notificationMapper.toNotificationResponse(notificationRepository.save(notification));

        notificationResponse.setSender(userMapper.toBasicUserResponse(notification.getSender()));
        notificationResponse.setReceiver(userMapper.toBasicUserResponse(notification.getReceiver()));


        if (notification.getContextType().equals(TableType.ARTICLE)) {
            Article article = articleRepository.findById(notification.getContextId()).orElse(null);
            notificationResponse.setContext(articleMapper.toArticleResponse(article));
        }

        if (notification.getDirectObjectType().equals(TableType.COMMENT)) {
            Comment comment = commentRepository.findById(notification.getDirectObjectId()).orElse(null);
            if(Objects.nonNull(comment)) {
                CommentDirect commentDirect =
                        CommentDirect.builder().direct_id(comment.getId()).parentId(comment.getCommentableId()).build();
                notificationResponse.setDirectObject(commentDirect);
            }

        }
        return notificationResponse;
    }

    @Override
    public boolean read(Long id) {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        User author = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        Notification notification = notificationRepository.findById(id).orElseThrow(() -> {
            return new AppException(ErrorCode.NOTIFICATION_NOT_FOUND);
        });

        if (author.equals(notification.getReceiver())) {
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
                new AppException(ErrorCode.USER_NOT_FOUND)
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
