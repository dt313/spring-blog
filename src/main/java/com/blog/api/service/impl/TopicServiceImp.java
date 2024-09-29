package com.blog.api.service.impl;

import com.blog.api.entities.Topic;
import com.blog.api.exception.AppException;
import com.blog.api.exception.ErrorCode;
import com.blog.api.repository.TopicRepository;
import com.blog.api.service.TopicService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class TopicServiceImp implements TopicService {
    TopicRepository topicRepository;
    @Override
    public List<Topic> getAll() {
        return topicRepository.findAll();
    }

    @Override
    public Topic getByName(String name) {

        return topicRepository.findById(name).orElseThrow(()
                -> new AppException(ErrorCode.TOPIC_NOT_FOUND));
    }

    @Override
    public void delete(String name) {
        boolean isExists = topicRepository.existsById(name);
        if(isExists) {
            topicRepository.deleteById(name);
        }else throw new AppException(ErrorCode.TOPIC_NOT_FOUND);
    }
}
