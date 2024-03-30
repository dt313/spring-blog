package com.blog.api.service;

import com.blog.api.exception.NotFoundException;
import com.blog.api.models.dto.ArticleDTO;
import com.blog.api.models.entity.Article;
import com.blog.api.models.entity.Metadata;
import com.blog.api.models.entity.Topic;
import com.blog.api.models.entity.User;
import com.blog.api.models.mapper.ArticleMapper;
import com.blog.api.models.mapper.UserMapper;
import com.blog.api.models.request.ArticleRequest;
import com.blog.api.repository.ArticleRepository;
import com.blog.api.repository.MetadataRepository;
import com.blog.api.repository.TopicRepository;
import com.blog.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ArticleServiceImp implements ArticleService{
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private MetadataRepository metadataRepository;


    @Override
    public List<ArticleDTO> getAllArticle() {
         List<Article> articles = articleRepository.findAll();
         List<ArticleDTO> result = new ArrayList<>();
        for (Article art : articles ) {
            result.add(ArticleMapper.toArticleDTO(art));
        }
         return result;
    }

    @Override
    public ArticleDTO getArticleById(Long id) {
        Optional<Article> isExist = articleRepository.findById(id);

        if(isExist.isPresent()) {
            return ArticleMapper.toArticleDTO(isExist.get());
        }else {
            throw new NotFoundException("Article Not Found with Id : " + id);
        }

    }

    @Override
    public ArticleDTO insertArticle(ArticleRequest newArticle) {
        System.out.println(newArticle);
        List<Article> isExist = articleRepository.findArticleByTitle(newArticle.getTitle().trim());
        User author = userRepository.findById(newArticle.getAuthor()).orElseThrow(() ->
                new NotFoundException("User Not Found with Id : " + newArticle.getAuthor())
        );
        if(isExist.isEmpty()) {
            Article result = new Article();
            // Article create
            result.setTitle(newArticle.getTitle().trim());
            result.setAuthor(author);
            result.setContent(newArticle.getContent());
            // Add Metadata
            result.setMetadata(newArticle.getMetadata());
            // Add Topic
            for(String topic : newArticle.getTopics()){
                if(topic.trim().equals("")) continue;
                Topic isTopicExist = topicRepository.findTopicByName(topic);
                if(isTopicExist == null) {
                    result.getTopics().add(new Topic(topic));
                }else {
                    isTopicExist.setCount(isTopicExist.getCount()+1);
                    result.getTopics().add(topicRepository.save(isTopicExist));
                }
            }
            return ArticleMapper.toArticleDTO(articleRepository.save(result));
        }
        else {
            throw new NotFoundException("Title duplicated : " + newArticle.getTitle());

        }
    }

    @Override
    public ArticleDTO updateArticle(Long id, ArticleRequest updateArticle) {

        Article updatedArticle = articleRepository.findById(id).map(article -> {
            article.setContent(updateArticle.getContent());
            article.setTitle(updateArticle.getTitle());
            return articleRepository.save(article);
        }).orElseGet(() -> {
            throw new NotFoundException("Article Not Found with Id : " + id);
        });
        return ArticleMapper.toArticleDTO(updatedArticle);
    }

    @Override
    public boolean deleteArticle(Long id) {
        boolean isExist = articleRepository.existsById(id);
        if(isExist) {
            articleRepository.deleteById(id);
            return true;
        }else {
            throw new NotFoundException("Article Not Found with Id : " + id);
        }
    }
}
