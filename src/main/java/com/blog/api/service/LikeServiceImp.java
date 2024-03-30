package com.blog.api.service;

import com.blog.api.exception.NotFoundException;
import com.blog.api.models.dto.LikeDTO;
import com.blog.api.models.dto.UserDTO;
import com.blog.api.models.entity.Article;
import com.blog.api.models.entity.Like;
import com.blog.api.models.entity.User;
import com.blog.api.models.mapper.LikeMapper;
import com.blog.api.models.mapper.UserMapper;
import com.blog.api.repository.ArticleRepository;
import com.blog.api.repository.LikeRepository;
import com.blog.api.repository.UserRepository;
import com.blog.api.types.TableType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.*;

@Component
public class LikeServiceImp implements LikeService{
    @Autowired
    private LikeRepository likeRepository;
    @Autowired

    private UserRepository userRepository;
    @Autowired

    private ArticleRepository articleRepository;
    @Override
    public LikeDTO toggleLike(Long userId, Long artId) {
        User isExistUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found id = " + userId));
        Article isExistArt = articleRepository.findById(artId).orElseThrow(() -> new NotFoundException("Article not found id = " + artId));
        Like isExistLike = likeRepository.findLikeByArtId(isExistArt);

        if(isExistLike == null) {
            Like newLike = new Like(isExistArt, TableType.QUESTION, isExistUser);
            return LikeMapper.toLikeDTO(likeRepository.save(newLike));
        }else {
            if(isExistLike.getIndexOfUserInLikedUsers(isExistUser) > -1) {
                isExistLike.getLikedUsers().remove(isExistUser);
                if(isExistLike.getLikedUsers().isEmpty()) {
                    likeRepository.delete(isExistLike);
                    return null;
                }
                return LikeMapper.toLikeDTO(likeRepository.save(isExistLike));

            }else {
                isExistLike.getLikedUsers().add(isExistUser);
                return LikeMapper.toLikeDTO(likeRepository.save(isExistLike));
            }
        }
    }

    @Override
    public Set<UserDTO> getAllLikedUser(Long artId) {
        Article isExistArt = articleRepository.findById(artId).orElseThrow(() -> new NotFoundException("Article not found id = " + artId));
        Like isExistLike = likeRepository.findLikeByArtId(isExistArt);

        Set<UserDTO> result = new HashSet<>();

        if(isExistLike != null) {
            for (User user : isExistLike.getLikedUsers()){
                result.add(UserMapper.toUserDTO(user));
            }
        }

        return result;

    }

    @Override
    public boolean isLiked(Long userId, Long artId) {
        User isExistUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found id = " + userId));
        Article isExistArt = articleRepository.findById(artId).orElseThrow(() -> new NotFoundException("Article not found id = " + artId));
        Like isExistLike = likeRepository.findLikeByArtId(isExistArt);
        if(isExistLike == null) return false;
        else {
            return isExistLike.getLikedUsers().contains(isExistUser);
        }
    }

    @Override
    public Integer getLengthOfArticle(Long artId) {
        Article isExistArt = articleRepository.findById(artId).orElseThrow(() -> new NotFoundException("Article not found id = " + artId));
        Like isExistLike = likeRepository.findLikeByArtId(isExistArt);
        if(isExistLike == null) {
            return 0;
        }
        else {
            return isExistLike.getLikedUsers().size();
        }
    }
}
