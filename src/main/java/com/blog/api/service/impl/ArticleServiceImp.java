package com.blog.api.service.impl;

import com.blog.api.dto.request.ArticleRequest;
import com.blog.api.dto.response.ArticleResponse;
import com.blog.api.entities.Article;
import com.blog.api.entities.Topic;
import com.blog.api.entities.User;
import com.blog.api.exception.AppException;
import com.blog.api.exception.ErrorCode;
import com.blog.api.mapper.ArticleMapper;
import com.blog.api.mapper.UserMapper;
import com.blog.api.repository.ArticleRepository;
import com.blog.api.repository.TopicRepository;
import com.blog.api.repository.UserRepository;
import com.blog.api.service.ArticleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
@Slf4j
public class ArticleServiceImp implements ArticleService {

    ArticleRepository articleRepository;
    UserRepository userRepository;
    TopicRepository topicRepository;
    ArticleMapper articleMapper;
    UserMapper userMapper;
    @Override
    public List<ArticleResponse> getAll() {
        List<ArticleResponse> articles = articleRepository.findAll().stream().map((article) -> {
           ArticleResponse temp = articleMapper.toArticleResponse(article);
           temp.setAuthor(userMapper.toBasicUserResponse(article.getAuthor()));
           return temp;
        }).toList();
        return articles;

    }

    @Override
    public ArticleResponse getById(String id) {
        Article article = articleRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.ARTICLE_NOT_FOUND));

        ArticleResponse response  = articleMapper.toArticleResponse(article);
        response.setAuthor(userMapper.toBasicUserResponse(article.getAuthor()));

        return response;
    }

    @Override
    public ArticleResponse create(ArticleRequest request) {
        log.info("info : "+request );
        boolean isExistArticle = articleRepository.existsByTitle(request.getTitle());
        if(isExistArticle) throw new AppException(ErrorCode.ARTICLE_EXISTS);
        User author = userRepository.findById(request.getAuthor()).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));

        // Author
        Article article = articleMapper.toArticle(request);
        article.setAuthor(author);
        // Topic
        Set<Topic> topics = getListTopics(request.getTopics());
        article.setTopics(topics);

        // Response
        ArticleResponse articleResponse = articleMapper.toArticleResponse(articleRepository.save(article));
        articleResponse.setAuthor(userMapper.toBasicUserResponse(author));

        return articleResponse;
    }

    @Override
    public ArticleResponse update(String id, ArticleRequest updateArticle) {
        Article article = articleRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.ARTICLE_NOT_FOUND));
        articleMapper.updateArticle(article, updateArticle);
        // Remove Topic
        removeTopics(updateArticle.getTopics());

       // Compare author
        if(!Objects.equals(article.getAuthor().getId(), updateArticle.getAuthor())) throw
                new AppException(ErrorCode.UNAUTHORIZED);

        // Author
        User author = userRepository.findById(updateArticle.getAuthor()).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));
        article.setAuthor(author);
       // Topic
        Set<Topic> topics = getListTopics(updateArticle.getTopics());
        article.setTopics(topics);

        // Response
        ArticleResponse articleResponse = articleMapper.toArticleResponse(articleRepository.save(article));
        articleResponse.setAuthor(userMapper.toBasicUserResponse(author));

        return articleResponse;



    }

    @Override
    public void delete(String id) {
        boolean isExists = articleRepository.existsById(id);
        if(isExists) {
            articleRepository.deleteById(id);
        }else {
            throw new AppException(ErrorCode.ARTICLE_NOT_FOUND);
        }
    }

    private Set<Topic> getListTopics(Set<String> request) {

        Set<Topic> topics = new HashSet<>();
        for(String topic : request) {
            Topic temp = topicRepository.findById(topic).orElse(null);
            if(Objects.nonNull(temp)) {
                temp.setCount(temp.getCount() + 1);
                topics.add(topicRepository.save(temp));
            }else {
                temp = topicRepository.save(Topic.builder().name(topic).count(1).build());
                topics.add(temp);
            }
        }
        return topics;
    }

    private void removeTopics(Set<String> request) {

        Set<Topic> topics = new HashSet<>();
        for(String topic : request) {
            Topic temp = topicRepository.findById(topic).orElse(null);
            if(Objects.nonNull(temp)) {
                if(temp.getCount() > 0) {
                    temp.setCount(temp.getCount() - 1);
                    topicRepository.save(temp);

                }else {
                    topicRepository.deleteById(topic);
                }
            }else continue;

        }
    }


}
