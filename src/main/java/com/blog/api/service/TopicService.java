package com.blog.api.service;

import com.blog.api.entities.Topic;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TopicService {
    List<Topic> getAll();
    Topic getByName(String name);
    void delete(String name);
}
