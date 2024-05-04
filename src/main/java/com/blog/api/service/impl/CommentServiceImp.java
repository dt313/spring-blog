package com.blog.api.service.impl;

import com.blog.api.dto.request.CommentCreationRequest;
import com.blog.api.dto.response.CommentResponse;
import com.blog.api.entities.Comment;
import com.blog.api.entities.User;
import com.blog.api.exception.AppException;
import com.blog.api.exception.ErrorCode;
import com.blog.api.mapper.CommentMapper;
import com.blog.api.mapper.UserMapper;
import com.blog.api.repository.ArticleRepository;
import com.blog.api.repository.CommentRepository;
import com.blog.api.repository.UserRepository;
import com.blog.api.service.CommentService;
import com.blog.api.types.TableType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

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

    @Override
    public CommentResponse create(CommentCreationRequest request) {
        User publisher = userRepository.findById(request.getPublisher()).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));

        boolean isExistsArt = articleRepository.existsById(request.getArtId());
        System.out.println("TRUE: " + isExistsArt);
        if(!isExistsArt) {
            throw new AppException(ErrorCode.ARTICLE_NOT_FOUND);
        }



        Comment newComment = commentMapper.toComment(request);
        newComment.setPublisher(publisher);
        newComment.setApproved(true);
        if (Objects.isNull(request.getParentId())) {
            newComment.setReply(false);
        } else {
            if (commentRepository.existsById(request.getParentId())) {
                newComment.setReply(true);
            } else throw new AppException(ErrorCode.COMMENT_NOT_FOUND);
        }

        System.out.println(TableType.valueOf(request.getCommentType().toString()));

        CommentResponse commentResponse = commentMapper.toCommentResponse(commentRepository.save(newComment));
        commentResponse.setPublisher(userMapper.toBasicUserResponse(publisher));

        System.out.println(commentResponse);
        return commentResponse;
    }


    @Override
    public List<CommentResponse> getCommentByTypeAndArtId(TableType type, String id, int pNumber, int pSize) {
        PageRequest pageRequest = PageRequest.of(pNumber, pSize);
        boolean isExistsArt = articleRepository.existsById(id);
        if(!isExistsArt) {
            throw new AppException(ErrorCode.ARTICLE_NOT_FOUND);
        }
        List<Comment> comments = commentRepository.findAllByCommentTypeAndArtId(type,id, pageRequest);

        List<CommentResponse> commentsResponse = comments.stream().map((comment) -> {
            CommentResponse commentResponse = commentMapper.toCommentResponse(commentRepository.save(comment));
            commentResponse.setPublisher(userMapper.toBasicUserResponse(comment.getPublisher()));
            return commentResponse;
        }).toList();
        return commentsResponse;
    }

    @Override
    public List<CommentResponse> getCommentsByArtAndParentId(
            TableType type, String artId, String parentId, int pNumber, int pSize
    ) {
        boolean isExistsArt = articleRepository.existsById(artId);
        if(!isExistsArt) {
            throw new AppException(ErrorCode.ARTICLE_NOT_FOUND);
        }

        PageRequest pageRequest = PageRequest.of(pNumber, pSize);
        List<Comment> comments = commentRepository.findAllByCommentTypeAndArtIdAndParentId(
                                                    type,artId,parentId,pageRequest);

        List<CommentResponse> commentsResponse = comments.stream().map((comment) -> {
            CommentResponse commentResponse = commentMapper.toCommentResponse(commentRepository.save(comment));
            commentResponse.setPublisher(userMapper.toBasicUserResponse(comment.getPublisher()));
            return commentResponse;
        }).toList();
        return commentsResponse;
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
    public CommentResponse getCommentById(String id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.COMMENT_NOT_FOUND));

        CommentResponse commentResponse = commentMapper.toCommentResponse(comment);
        commentResponse.setPublisher(userMapper.toBasicUserResponse(comment.getPublisher()));
        return commentResponse;
    }

    @Override

    public void delete(String id) {
        boolean isExists =  commentRepository.existsById(id);
        if(isExists) {
            commentRepository.deleteById(id);
        }else throw new AppException(ErrorCode.COMMENT_NOT_FOUND);
    }
}
