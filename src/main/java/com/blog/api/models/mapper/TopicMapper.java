package com.blog.api.models.mapper;

import com.blog.api.models.dto.TopicDTO;
import com.blog.api.models.entity.Topic;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TopicMapper {

    public static Set<TopicDTO> toListTopicDTO(Set<Topic> topicSet) {
        Set<TopicDTO> result = new HashSet<>();
        for (Topic topic: topicSet) {
            TopicDTO newTopicDTO = new TopicDTO();
            newTopicDTO.setId(topic.getId());
            newTopicDTO.setName(topic.getName());
            result.add(newTopicDTO);
        }
        return result;
    }


}
