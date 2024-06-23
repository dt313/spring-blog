package com.blog.api.service.impl;

import com.blog.api.dto.request.ArticleRequest;
import com.blog.api.dto.request.QuestionRequest;
import com.blog.api.dto.response.ArticleResponse;
import com.blog.api.dto.response.QuestionResponse;
import com.blog.api.entities.Article;
import com.blog.api.entities.Question;
import com.blog.api.entities.Topic;
import com.blog.api.entities.User;
import com.blog.api.exception.AppException;
import com.blog.api.exception.ErrorCode;
import com.blog.api.mapper.QuestionMapper;
import com.blog.api.mapper.UserMapper;
import com.blog.api.repository.QuestionRepository;
import com.blog.api.repository.TopicRepository;
import com.blog.api.repository.UserRepository;
import com.blog.api.service.QuestionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
@Slf4j
public class QuestionServiceImp implements QuestionService {
    UserRepository userRepository;
    QuestionRepository questionRepository;
    TopicRepository topicRepository;
    UserMapper userMapper;
    QuestionMapper questionMapper;
    @Override
    public List<QuestionResponse> getAll(String searchValue, int pageNumber, int pageSize) {
        Sort sort = Sort.by("createdAt").descending();
        PageRequest pageRequest = PageRequest.of(pageNumber -1, pageSize, sort);

        List<QuestionResponse> questions = questionRepository.findByContentContaining(searchValue,pageRequest)
                .stream().map((question) -> {
            QuestionResponse temp = questionMapper.toQuestionResponse(question);
            temp.setAuthor(userMapper.toBasicUserResponse(question.getAuthor()));
            return temp;
        }).toList();
        return questions;
    }

    @Override
    public List<QuestionResponse> getAllByAuthor(String id) {
        User author = userRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));

        List<QuestionResponse> questions = questionRepository.findAllByAuthor(author).stream().map((question) -> {
            QuestionResponse temp = questionMapper.toQuestionResponse(question);
            temp.setAuthor(userMapper.toBasicUserResponse(question.getAuthor()));
            return temp;
        }).toList();
        return questions;
    }

    @Override
    public List<QuestionResponse> getAllByTopic(String name, int pageNumber, int pageSize) {
        Sort sort = Sort.by("createdAt").descending();
        PageRequest pageRequest = PageRequest.of(pageNumber -1, pageSize, sort);
        List<QuestionResponse> questions = questionRepository.findByTopicName(name, pageRequest).stream().map((question) -> {
            QuestionResponse temp = questionMapper.toQuestionResponse(question);
            temp.setAuthor(userMapper.toBasicUserResponse(question.getAuthor()));
            return temp;
        }).toList();
        return questions;
    }

    @Override
    public QuestionResponse getById(String id) {
        Question question = questionRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.ARTICLE_NOT_FOUND));

        QuestionResponse questionResponse = questionMapper.toQuestionResponse(question);
        questionResponse.setAuthor(userMapper.toBasicUserResponse(question.getAuthor()));

        return questionResponse;
    }

    @Override
    public QuestionResponse create(QuestionRequest request) {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        System.out.println(username);
        User author = userRepository.findByUsername(username).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));
        // Author
        Question article = questionMapper.toQuestion(request);
        article.setAuthor(author);
        // Topic
        Set<Topic> topics = getListTopics(request.getTopics());
        article.setTopics(topics);
        // Response
        QuestionResponse questionResponse = questionMapper.toQuestionResponse(questionRepository.save(article));
        questionResponse.setAuthor(userMapper.toBasicUserResponse(author));

        return questionResponse;
    }

    @Override
    public QuestionResponse update(String id, QuestionRequest request) {
        System.out.println(request);
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        // Author
        User author = userRepository.findByUsername(username).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));

        Question question = questionRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.ARTICLE_NOT_FOUND));

        questionMapper.updateQuestion(question, request);
        // Compare author
        if(!Objects.equals(question.getAuthor().getUsername(), username)) throw
                new AppException(ErrorCode.UNAUTHORIZED);

        // Remove Topic
        deleteOldTopic(question.getTopics());

        // Topic
        Set<Topic> topics = getListTopics(request.getTopics());
        question.setTopics(topics);
        // Response
        QuestionResponse questionResponse = questionMapper.toQuestionResponse(questionRepository.save(question));
        questionResponse.setAuthor(userMapper.toBasicUserResponse(author));

        return questionResponse;
    }

    @Override
    public void delete(String id) {
        boolean isExists = questionRepository.existsById(id);
        if(isExists) {
            questionRepository.deleteById(id);
        }else {
            throw new AppException(ErrorCode.ARTICLE_NOT_FOUND);
        }
    }

    @Override
    public Integer getLengthBySearchValue(String searchValue) {
        List<Question> questions = questionRepository.findByContentContaining(searchValue);
        return questions.size();
    }

    @Override
    public Integer getLengthByTopic(String topicName) {
        List<Question> questions = questionRepository.findByTopicName(topicName);
        return questions.size();
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

    private void deleteOldTopic(Set<Topic> request) {
        System.out.println(request);
        Set<Topic> topics = new HashSet<>();
        for(Topic topic : request) {
            Topic temp = topicRepository.findById(topic.getName()).orElse(null);
            System.out.println(temp);
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
}
