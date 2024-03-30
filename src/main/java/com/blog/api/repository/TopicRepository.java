package com.blog.api.repository;

import com.blog.api.models.entity.Metadata;
import com.blog.api.models.entity.Topic;
import com.blog.api.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    Topic findTopicByName(String name);
}
