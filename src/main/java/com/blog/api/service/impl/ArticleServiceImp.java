package com.blog.api.service.impl;

import com.blog.api.dto.request.ArticleRequest;
import com.blog.api.dto.request.SuggestionRequest;
import com.blog.api.dto.response.ArticleResponse;
import com.blog.api.dto.response.ReactionResponse;
import com.blog.api.entities.Article;
import com.blog.api.entities.Reaction;
import com.blog.api.entities.Topic;
import com.blog.api.entities.User;
import com.blog.api.exception.AppException;
import com.blog.api.exception.ErrorCode;
import com.blog.api.mapper.ArticleMapper;
import com.blog.api.mapper.ReactionMapper;
import com.blog.api.mapper.UserMapper;
import com.blog.api.repository.ArticleRepository;
import com.blog.api.repository.ReactionRepository;
import com.blog.api.repository.TopicRepository;
import com.blog.api.repository.UserRepository;
import com.blog.api.service.ArticleService;
import com.blog.api.types.ReactionType;
import com.blog.api.utils.TableUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.time.*;
import java.time.format.DateTimeFormatter;
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
    ReactionRepository reactionRepository;
    UserMapper userMapper;
    TableUtils tableUtils;
    ReactionMapper reactionMapper;
    @Override
    public List<ArticleResponse> getAll(String searchValue ,int pageNumber, int pageSize) {

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElse(null);

        Sort sort = Sort.by("createdAt").descending();
        PageRequest pageRequest = PageRequest.of(pageNumber -1, pageSize, sort);
        List<ArticleResponse> articles = articleRepository.findByTitleContainingAndIsPublished(searchValue,true,pageRequest).stream().map((article) -> {
           ArticleResponse temp = articleMapper.toArticleResponse(article);
            temp.setBookmarked(tableUtils.checkIsBookmarked(temp.getId(), user ));
            temp.setReacted(tableUtils.checkIsReacted(temp.getId(), user ));
           temp.setAuthor(userMapper.toBasicUserResponse(article.getAuthor()));
           return temp;
        }).toList();
        return articles;

    }

    @Override
    public List<ArticleResponse> getAllFeaturedArticle(String searchValue, int pageNumber, int pageSize) {

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElse(null);

        Sort sort = Sort.by("reactionCount", "commentCount").descending();

        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, sort);
        List<ArticleResponse> articles = articleRepository.findByTitleContainingAndIsPublished(searchValue, true, pageRequest).stream().map((article) -> {
            ArticleResponse temp = articleMapper.toArticleResponse(article);
            temp.setBookmarked(tableUtils.checkIsBookmarked(temp.getId(), user));
            temp.setReacted(tableUtils.checkIsReacted(temp.getId(), user));
            temp.setAuthor(userMapper.toBasicUserResponse(article.getAuthor()));

            return temp;
        }).toList();




        return articles;
    }

    @Override
    public List<ArticleResponse> getAllByTopic(String name,int pageNumber, int pageSize ) {

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElse(null);

        Sort sort = Sort.by("createdAt").descending();
        PageRequest pageRequest = PageRequest.of(pageNumber -1, pageSize, sort);
        List<ArticleResponse> articles = articleRepository.findByTopicName(name, pageRequest).stream().map((article) -> {
            ArticleResponse temp = articleMapper.toArticleResponse(article);
            temp.setBookmarked(tableUtils.checkIsBookmarked(temp.getId(), user ));
            temp.setReacted(tableUtils.checkIsReacted(temp.getId(), user ));
            temp.setAuthor(userMapper.toBasicUserResponse(article.getAuthor()));
            return temp;
        }).toList();

        return articles;

    }

    // without permission
    @Override
    public ArticleResponse getById(Long id) {

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        User user = userRepository.findByUsername(username).orElse(null);
        Article article = articleRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.ARTICLE_NOT_FOUND));
        article.setBookmarked(tableUtils.checkIsBookmarked(article.getId(), user));
        article.setReacted(tableUtils.checkIsReacted(article.getId(), user ));
        ArticleResponse response  = articleMapper.toArticleResponse(article);
        Reaction reaction = reactionRepository.findByReactionTableIdAndReactedUser(article.getId(), user);
        if(Objects.nonNull(reaction)) {
            response.setReactedType(reaction.getType());
        }else {
            response.setReactedType(ReactionType.NULL);
        }

        response.setAuthor(userMapper.toBasicUserResponse(article.getAuthor()));

        return response;
    }

    // without permission
    @Override
    public ArticleResponse getBySlug(String slug) {

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        User user = userRepository.findByUsername(username).orElse(null);
        Article article = articleRepository.findBySlugAndIsPublished(slug, true).orElseThrow(
                () -> new AppException(ErrorCode.ARTICLE_NOT_FOUND));


        article.setBookmarked(tableUtils.checkIsBookmarked(article.getId(), user));
        article.setReacted(tableUtils.checkIsReacted(article.getId(), user));
        ArticleResponse response = articleMapper.toArticleResponse(article);
        response.setAuthor(userMapper.toBasicUserResponse(article.getAuthor()));

        // reacted type of user
        Reaction reaction = reactionRepository.findByReactionTableIdAndReactedUser(article.getId(), user);


        if(Objects.nonNull(reaction)) {
            response.setReactedType(reaction.getType());
        }else {
            response.setReactedType(ReactionType.NULL);
        }

        // reacted users
        List<ReactionResponse> reactionsResponse = article.getReactions().stream().map((r) -> {
            ReactionResponse reactionResponse = reactionMapper.toReactionResponse(r);
            reactionResponse.setReactedUser(userMapper.toBasicUserResponse(r.getReactedUser()));
            return reactionResponse;
        }).toList();

        response.setReactions(new HashSet<>(reactionsResponse));

        return response;
    }

    @Override
    public ArticleResponse getBySlugWithAuth(String slug) {

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        User user = userRepository.findByUsername(username).orElseThrow(() ->  new AppException(ErrorCode.UNAUTHORIZED));

        Article article = articleRepository.findBySlug(slug).orElseThrow(
                () -> new AppException(ErrorCode.ARTICLE_NOT_FOUND));


        article.setBookmarked(tableUtils.checkIsBookmarked(article.getId(), user));
        article.setReacted(tableUtils.checkIsReacted(article.getId(), user));
        ArticleResponse response = articleMapper.toArticleResponse(article);
        response.setAuthor(userMapper.toBasicUserResponse(article.getAuthor()));

        // reacted type of user
        Reaction reaction = reactionRepository.findByReactionTableIdAndReactedUser(article.getId(), user);
        if(Objects.nonNull(reaction)) {
            response.setReactedType(reaction.getType());
        }else {
            response.setReactedType(ReactionType.NULL);
        }

        // reacted users
        List<ReactionResponse> reactionsResponse = article.getReactions().stream().map((r) -> {
            ReactionResponse reactionResponse = reactionMapper.toReactionResponse(r);
            reactionResponse.setReactedUser(userMapper.toBasicUserResponse(r.getReactedUser()));
            return reactionResponse;
        }).toList();

        response.setReactions(new HashSet<>(reactionsResponse));

        return response;
    }

    @Override
    public List<ArticleResponse> getAllArticleByAuthor(Long id) {

        User author = userRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));

        List<ArticleResponse> articles = articleRepository.findAllByAuthor(author).stream().map((article) -> {
            ArticleResponse temp = articleMapper.toArticleResponse(article);
            temp.setAuthor(userMapper.toBasicUserResponse(article.getAuthor()));
            return temp;
        }).toList();

        return articles;
    }

    @Override
    public List<ArticleResponse> getSuggestionsArticle(SuggestionRequest request) {

        PageRequest pageRequest = PageRequest.of(0, 10);

        List<ArticleResponse> articles = articleRepository.findByTopicsOrAuthor(
                request.getTopics(), request.getUserId(),pageRequest).stream().map((article)
                -> {
            ArticleResponse temp = articleMapper.toArticleResponse(article);
            temp.setAuthor(userMapper.toBasicUserResponse(article.getAuthor()));
            return temp;
        }).toList();

        return articles;
    }
    @Override
    public ArticleResponse create(ArticleRequest request) {

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        boolean isExistArticle = articleRepository.existsByTitle(request.getTitle());
        if(isExistArticle) throw new AppException(ErrorCode.ARTICLE_EXISTS);

        User author = userRepository.findByUsername(username).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));
        // Author
        Article article = articleMapper.toArticle(request);
        article.setSlug(generateSlug(article.getTitle()));
        article.setAuthor(author);
        // Topic
        Set<Topic> topics = getListTopics(request.getTopics());
        article.setTopics(topics);

        LocalDateTime localDateTime = LocalDateTime.parse(request.getPublishAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        // Đặt múi giờ KST
        ZonedDateTime kstDateTime = localDateTime.atZone(ZoneId.of("Asia/Seoul"));

        // Chuyển đổi thời gian KST sang UTC
        ZonedDateTime utcDateTime = kstDateTime.withZoneSameInstant(ZoneId.of("UTC"));

        article.setPublishAt(utcDateTime.toInstant());

        if (article.getPublishAt().isAfter(Instant.now())) {
            article.setPublished(false);
        }else {
            article.setPublished(true);
        }
        // Response
        ArticleResponse articleResponse = articleMapper.toArticleResponse(articleRepository.save(article));
        articleResponse.setAuthor(userMapper.toBasicUserResponse(author));

        return articleResponse;
    }

    @Scheduled(fixedRate = 60000) // chạy mỗi 60 giây
    public void schedulePostArticle() {
        List<Article> articles = articleRepository.findByIsPublished(false);
        if(Objects.nonNull(articles)) {
            articles.stream().forEach((article) -> {
                if(article.getPublishAt().isBefore(Instant.now())) {
                    article.setPublished(true);
                    articleRepository.save(article);
                }
            });
        }else return;
    }

    @Override
    public ArticleResponse update(String slug, ArticleRequest updateArticle) {

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        // Author
        User author = userRepository.findByUsername(username).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));

        Article article = articleRepository.findBySlug(slug).orElseThrow(() ->
                new AppException(ErrorCode.ARTICLE_NOT_FOUND));

        article.setBookmarked(tableUtils.checkIsBookmarked(article.getId(), author ));
        article.setReacted(tableUtils.checkIsReacted(article.getId(), author ));
        article.setSlug(generateSlug(updateArticle.getTitle()));

        articleMapper.updateArticle(article, updateArticle);

       // Compare author
        if(!Objects.equals(article.getAuthor().getUsername(), username)) throw
                new AppException(ErrorCode.USER_CONFLICT);
        // Remove Topic
        deleteOldTopic(article.getTopics(), article);
       // Topic
        Set<Topic> topics = getListTopics(updateArticle.getTopics());
        article.setTopics(topics);
        // Response
        ArticleResponse articleResponse = articleMapper.toArticleResponse(articleRepository.save(article));
        articleResponse.setAuthor(userMapper.toBasicUserResponse(author));

        return articleResponse;
    }

    @Override
    public ArticleResponse publish(Long id) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ARTICLE_NOT_FOUND));
        article.setPublished(true);
        article.setPublishAt(Instant.now());
        articleRepository.save(article);
        return articleMapper.toArticleResponse(article);
    }

    @Override
    public void delete(Long id) {
        Article article = articleRepository.findById(id).orElseThrow(
                ()-> new AppException(ErrorCode.ARTICLE_NOT_FOUND)
        );
        if(Objects.nonNull(article)) {
            deleteOldTopic(article.getTopics(), article);
            articleRepository.deleteById(id);
        }else {
            throw new AppException(ErrorCode.ARTICLE_NOT_FOUND);
        }
    }

    @Override
    public Integer lengthOfArticleBySearchValue(String searchValue) {
        List<Article> articles = articleRepository.findByTitleContainingAndIsPublished(searchValue, true);
        return articles.size();
    }

    @Override
    public Integer lengthOfArticleByTopic(String searchValue) {
        List<Article> articles = articleRepository.findByTopicName(searchValue);
        return articles.size();
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

    private void deleteOldTopic(Set<Topic> request, Article article) {
        Set<Topic> emptyTopics = new HashSet<>();
        article.setTopics(emptyTopics);
        articleRepository.save(article);

        for(Topic topic : request) {
            Topic temp = topicRepository.findById(topic.getName()).orElse(null);
            if(Objects.nonNull(temp)) {
                if(temp.getCount() > 1) {
                    temp.setCount(temp.getCount() - 1);
                    topicRepository.save(temp);

                }else {
                    topicRepository.deleteById(temp.getName());
                }
            }else continue;

        }
    }


    private String generateSlug(String title) {
        String slug = title.toLowerCase();
        // Xóa dấu tiếng Việt
        slug = Normalizer.normalize(slug, Normalizer.Form.NFD);
        slug = slug.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        // Thay thế các ký tự không phải là chữ cái và số bằng dấu gạch ngang
        slug = slug.replaceAll("[^a-z0-9\\s-]", "").replaceAll("[\\s]+", "-");
        // Loại bỏ các dấu gạch ngang thừa
        slug = slug.replaceAll("[-]+", "-");
        // Loại bỏ dấu gạch ngang ở đầu và cuối chuỗi (nếu có)
        slug = slug.replaceAll("^-|-$", "");
        return slug;
    }



}
