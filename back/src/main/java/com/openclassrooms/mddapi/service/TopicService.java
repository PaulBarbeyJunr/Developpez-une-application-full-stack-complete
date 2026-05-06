package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.TopicDto;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;

    public List<TopicDto> getAllTopics() {
        return topicRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private TopicDto toDto(Topic topic) {
        return TopicDto.builder()
                .id(topic.getId())
                .title(topic.getTitle())
                .description(topic.getDescription())
                .build();
    }
}
