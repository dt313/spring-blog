package com.blog.api.service;

import com.blog.api.exception.NotFoundException;
import com.blog.api.models.dto.CommentDTO;
import com.blog.api.models.entity.Article;
import com.blog.api.models.entity.Comment;
import com.blog.api.models.entity.User;
import com.blog.api.models.mapper.CommentMapper;
import com.blog.api.models.request.CommentRequest;
import com.blog.api.repository.ArticleRepository;
import com.blog.api.repository.CommentRepository;
import com.blog.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommentServiceImp implements CommentService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Override
    public CommentDTO createComment(CommentRequest newComment) {
        User isExistUser = userRepository.findById(newComment.getCmtUser()).orElseThrow(() -> new NotFoundException("User not found id = " + newComment.getCmtUser()));
        Article isExistArt = articleRepository.findById(newComment.getArtId()).orElseThrow(() -> new NotFoundException("Article not found id = " + newComment.getArtId()));
        // commented on article
        if(newComment.getParentId() != null && newComment.getParentId() != 0) {
            Comment parentComment = commentRepository.findById(newComment.getParentId()).orElseThrow(() -> new NotFoundException("Comment not found id = " + newComment.getParentId()));
            Comment reply = new Comment(isExistArt,isExistUser, newComment.getParentId(), newComment.getTableType(), newComment.getContent());
            parentComment.getReplies().add(commentRepository.save(reply));
            System.out.println("parentComment : " );
            return CommentMapper.toCommentDTO(commentRepository.save(parentComment)) ;

        }else { // reply
            return CommentMapper.toCommentDTO(
                    commentRepository.save(new Comment(isExistArt,isExistUser,0L, newComment.getTableType(), newComment.getContent()))
            );
        }
    }

    @Override
    public List<CommentDTO> getAllCommentByArt(Long id) {
        return null;
    }

    @Override
    public List<CommentDTO> getAllCommentByParent(Long id) {
        return null;
    }

    @Override
    public List<CommentDTO> getCommentByCommentId(Long id) {
        Article isExistArt = articleRepository.findById(id).orElseThrow(() -> new NotFoundException("Article not found id = " + id));
        List<Comment> cmtList = commentRepository.findCommentByParentIdAndArtId(0L, isExistArt);
        List <CommentDTO> result = new ArrayList<>();
        for (Comment cmt : cmtList) {
            result.add(CommentMapper.toCommentDTO(cmt));
        }
        return result;
    }
    @Override
    public List<CommentDTO> getAllComment() {
           List<Comment> cmtList =  commentRepository.findAll();
           List<CommentDTO> result = new ArrayList<>();
           for (Comment cmt : cmtList) {
               result.add(CommentMapper.toCommentDTO(cmt));
           }
           return result;
    }

    @Override
    public boolean deleteCommentById(Long id) {
        Comment isExistComment = commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Comment not found id = " + id));
        commentRepository.deleteById(id);
        return true;
    }
}
