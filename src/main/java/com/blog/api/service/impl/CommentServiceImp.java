package com.blog.api.service.impl;

import com.blog.api.dto.request.CommentCreationRequest;
import com.blog.api.dto.response.CommentResponse;
import com.blog.api.dto.response.ReactionResponse;
import com.blog.api.entities.Comment;
import com.blog.api.entities.Reaction;
import com.blog.api.entities.User;
import com.blog.api.exception.AppException;
import com.blog.api.exception.ErrorCode;
import com.blog.api.mapper.CommentMapper;
import com.blog.api.mapper.ReactionMapper;
import com.blog.api.mapper.UserMapper;
import com.blog.api.repository.*;
import com.blog.api.service.CommentService;
import com.blog.api.types.ReactionType;
import com.blog.api.types.TableType;
import com.blog.api.utils.TableUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceImp implements CommentService {
    CommentRepository commentRepository;
    UserRepository userRepository;
    ArticleRepository articleRepository;
    UserMapper userMapper;
    CommentMapper commentMapper;
    ReactionRepository reactionRepository;
    TableUtils tableUtils;
    ReactionMapper reactionMapper;
    NotificationRepository notificationRepository;

    @Override
    public CommentResponse create(CommentCreationRequest request) {

        User publisher = userRepository.findById(request.getPublisher()).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));

        Object isExist = new Object();

        if(request.getCommentType().equals(TableType.ARTICLE)) {
            isExist = articleRepository.findById(request.getCommentableId()).orElseThrow(() ->
                    new AppException(ErrorCode.ARTICLE_NOT_FOUND));
        }else if(request.getCommentType().equals(TableType.COMMENT)) {
            isExist = commentRepository.findById(request.getCommentableId()).orElseThrow(() ->
                    new AppException(ErrorCode.COMMENT_NOT_FOUND));
        }

        if(Objects.nonNull(isExist)) {
            Comment newComment = commentMapper.toComment(request);
            newComment.setPublisher(publisher);
            newComment.setApproved(true);
            newComment.setReplies(new ArrayList<>());
            newComment.setRepliesCount(0);
            newComment.setReactionCount(0);
            CommentResponse commentResponse = commentMapper.toCommentResponse(commentRepository.save(newComment));
            commentResponse.setPublisher(userMapper.toBasicUserResponse(publisher));
            commentResponse.setReactions(new HashSet<>());

            return commentResponse;
        }
        return null;
    }


    @Override
    public List<CommentResponse> getByTypeAndCommentableId(TableType type, Long id, int pNumber, int pSize) {
        Sort sort;
        if(type.equals(TableType.ARTICLE)) {
            sort = Sort.by("createdAt").descending();
        }else {
            sort = Sort.by("createdAt");
        }
        PageRequest pageRequest = PageRequest.of(pNumber - 1, pSize, sort);

        Object isExist = new Object();
        if(type.equals(TableType.ARTICLE)) {
            isExist = articleRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.ARTICLE_NOT_FOUND));
        }else if(type.equals(TableType.COMMENT)) {
            isExist = commentRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.COMMENT_NOT_FOUND));
        }

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User commentator = userRepository.findByUsername(username).orElse(null);

        if(Objects.nonNull(isExist)) {
            List<Comment> comments = commentRepository.findAllByCommentTypeAndCommentableId(type,id, pageRequest);
            List<CommentResponse> commentsResponse = comments.stream().map((comment) -> {
                comment.setReacted(tableUtils.checkIsReacted(comment.getId(), commentator));
                CommentResponse commentResponse = commentMapper.toCommentResponse(commentRepository.save(comment));
                commentResponse.setPublisher(userMapper.toBasicUserResponse(comment.getPublisher()));

                Reaction reaction = reactionRepository.findByReactionTableIdAndReactedUser(commentResponse.getId(), commentator);

                if(Objects.nonNull(reaction)) {
                    commentResponse.setReactedType(reaction.getType());
                }else {
                    commentResponse.setReactedType(ReactionType.NULL);
                }

                // reacted users
                List<ReactionResponse> reactionsResponse = comment.getReactions().stream().map((r) -> {
                    ReactionResponse reactionResponse = reactionMapper.toReactionResponse(r);
                    reactionResponse.setReactedUser(userMapper.toBasicUserResponse(r.getReactedUser()));
                    return reactionResponse;
                }).toList();

                commentResponse.setReactions(new HashSet<>(reactionsResponse));

                return commentResponse;
            }).toList();

            return commentsResponse;
        }else
            return null;
    }


    @Override
    public List<CommentResponse> getAllComment() {
        List<Comment> comments = commentRepository.findAll();

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User commentator = userRepository.findByUsername(username).orElse(null);

        List<CommentResponse> commentsResponse = comments.stream().map((comment) -> {
            comment.setReacted(tableUtils.checkIsReacted(comment.getCommentableId(), commentator));

            CommentResponse commentResponse = commentMapper.toCommentResponse(commentRepository.save(comment));
            commentResponse.setPublisher(userMapper.toBasicUserResponse(comment.getPublisher()));

            return commentResponse;
        }).toList();

        return commentsResponse;
    }

    @Override
    public CommentResponse getById(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.COMMENT_NOT_FOUND));

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User commentator = userRepository.findByUsername(username).orElse(null);

        comment.setReacted(tableUtils.checkIsReacted(comment.getCommentableId(), commentator));

        CommentResponse commentResponse = commentMapper.toCommentResponse(comment);
        commentResponse.setPublisher(userMapper.toBasicUserResponse(comment.getPublisher()));
        return commentResponse;
    }

    @Override
    public void delete(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        if(username.equals(comment.getPublisher().getUsername())) {
            commentRepository.deleteById(id);
        }else throw new AppException(ErrorCode.COMMENT_NOT_FOUND);
    }



    public Integer getLengthByCommentTypeAndCommentableId(TableType type, Long id) {
        List<Comment> listComment = commentRepository.findAllByCommentTypeAndCommentableId(type, id);
        return listComment.size();
    }
}