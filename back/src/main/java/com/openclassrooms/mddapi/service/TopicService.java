package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.TopicDto;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service métier de gestion des thèmes :
 * consultation, abonnement et désabonnement de l'utilisateur courant.
 */
@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    /**
     * Renvoie la liste de tous les thèmes, en indiquant pour chacun
     * si l'utilisateur courant y est abonné.
     */
    @Transactional(readOnly = true)
    public List<TopicDto> getAllTopics() {
        User currentUser = loadCurrentUser();
        Set<Long> subscribedIds = currentUser.getSubscriptions().stream()
                .map(Topic::getId)
                .collect(Collectors.toSet());

        return topicRepository.findAll().stream()
                .map(topic -> toDto(topic, subscribedIds.contains(topic.getId())))
                .collect(Collectors.toList());
    }

    /**
     * Abonne l'utilisateur courant au thème indiqué.
     * Idempotent : ne fait rien si l'utilisateur est déjà abonné.
     *
     * @param topicId identifiant du thème à suivre
     * @throws EntityNotFoundException si le thème n'existe pas
     */
    @Transactional
    public void subscribe(Long topicId) {
        User user = loadCurrentUser();
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new EntityNotFoundException("Thème introuvable"));
        user.getSubscriptions().add(topic);
        userRepository.save(user);
    }

    /**
     * Désabonne l'utilisateur courant du thème indiqué.
     * Idempotent : ne fait rien si l'utilisateur n'était pas abonné.
     *
     * @param topicId identifiant du thème à ne plus suivre
     * @throws EntityNotFoundException si le thème n'existe pas
     */
    @Transactional
    public void unsubscribe(Long topicId) {
        User user = loadCurrentUser();
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new EntityNotFoundException("Thème introuvable"));
        user.getSubscriptions().remove(topic);
        userRepository.save(user);
    }

    private User loadCurrentUser() {
        Long id = SecurityUtils.getCurrentUserId();
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));
    }

    private TopicDto toDto(Topic topic, boolean isSubscribed) {
        return TopicDto.builder()
                .id(topic.getId())
                .title(topic.getTitle())
                .description(topic.getDescription())
                .isSubscribed(isSubscribed)
                .build();
    }
}
