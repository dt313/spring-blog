package com.blog.api.service.impl;

import com.blog.api.dto.request.CommentCreationRequest;
import com.blog.api.dto.response.CommentResponse;
import com.blog.api.entities.Article;
import com.blog.api.entities.Comment;
import com.blog.api.entities.User;
import com.blog.api.exception.AppException;
import com.blog.api.exception.ErrorCode;
import com.blog.api.mapper.CommentMapper;
import com.blog.api.mapper.UserMapper;
import com.blog.api.repository.ArticleRepository;
import com.blog.api.repository.CommentRepository;
import com.blog.api.repository.QuestionRepository;
import com.blog.api.repository.UserRepository;
import com.blog.api.service.CommentService;
import com.blog.api.types.TableType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.aop.framework.AopConfigException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceImp implements CommentService {
    CommentRepository commentRepository;
    QuestionRepository questionRepository;
    UserRepository userRepository;
    ArticleRepository articleRepository;
    UserMapper userMapper;
    CommentMapper commentMapper;

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
        }else if(request.getCommentType().equals(TableType.QUESTION)) {
            isExist = questionRepository.findById(request.getCommentableId()).orElseThrow(() ->
                    new AppException(ErrorCode.QUESTION_NOT_FOUND));
        }

        if(Objects.nonNull(isExist)) {
            Comment newComment = commentMapper.toComment(request);
            newComment.setPublisher(publisher);
            newComment.setApproved(true);

            CommentResponse commentResponse = commentMapper.toCommentResponse(commentRepository.save(newComment));
            commentResponse.setPublisher(userMapper.toBasicUserResponse(publisher));
            return commentResponse;
        }
        return null;
    }


    @Override
    public List<CommentResponse> getByTypeAndCommentableId(TableType type, String id, int pNumber, int pSize) {
        Sort sort;
        if(type.equals(TableType.ARTICLE) || type.equals(TableType.QUESTION)) {
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
        }else if(type.equals(TableType.QUESTION)) {
            isExist = questionRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));
        }

        if(Objects.nonNull(isExist)) {
            List<Comment> comments = commentRepository.findAllByCommentTypeAndCommentableId(type,id, pageRequest);

            List<CommentResponse> commentsResponse = comments.stream().map((comment) -> {
                CommentResponse commentResponse = commentMapper.toCommentResponse(commentRepository.save(comment));
                commentResponse.setPublisher(userMapper.toBasicUserResponse(comment.getPublisher()));
                return commentResponse;
            }).toList();
            return commentsResponse;
        }else return null;
    }


    @Override
    public List<CommentResponse> getAllComment() {
        List<Comment> comments = commentRepository.findAll();

        List<CommentResponse> commentsResponse = comments.stream().map((comment) -> {
            CommentResponse commentResponse = commentMapper.toCommentResponse(commentRepository.save(comment));
            commentResponse.setPublisher(userMapper.toBasicUserResponse(comment.getPublisher()));
            return commentResponse;
        }).toList();
        return commentsResponse;
    }

    @Override
    public CommentResponse getById(String id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.COMMENT_NOT_FOUND));

        CommentResponse commentResponse = commentMapper.toCommentResponse(comment);
        commentResponse.setPublisher(userMapper.toBasicUserResponse(comment.getPublisher()));
        return commentResponse;
    }

    @Override

    public void delete(String id) {
        boolean isExists = commentRepository.existsById(id);
        if(isExists) {
            commentRepository.deleteById(id);
        }else throw new AppException(ErrorCode.COMMENT_NOT_FOUND);
    }

    public Integer getLengthByCommentTypeAndCommentableId(TableType type, String id) {
        List<Comment> listComment = commentRepository.findAllByCommentTypeAndCommentableId(type, id);
        return listComment.size();
    }
}
