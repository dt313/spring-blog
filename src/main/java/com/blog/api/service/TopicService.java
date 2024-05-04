package com.blog.api.service;

import com.blog.api.entities.Topic;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TopicService {
    public List<Topic> getAll();
    public Topic getByName(String name);
    public void delete(String name);
}
