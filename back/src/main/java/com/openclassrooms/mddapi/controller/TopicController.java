package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.TopicDto;
import com.openclassrooms.mddapi.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la consultation et l'abonnement aux thèmes.
 */
@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    /**
     * Renvoie la liste de tous les thèmes, avec pour chacun
     * le statut d'abonnement de l'utilisateur courant.
     */
    @GetMapping
    public ResponseEntity<List<TopicDto>> getAllTopics() {
        return ResponseEntity.ok(topicService.getAllTopics());
    }

    /**
     * Abonne l'utilisateur courant au thème indiqué.
     */
    @PostMapping("/{id}/subscribe")
    public ResponseEntity<Void> subscribe(@PathVariable Long id) {
        topicService.subscribe(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Désabonne l'utilisateur courant du thème indiqué.
     */
    @DeleteMapping("/{id}/subscribe")
    public ResponseEntity<Void> unsubscribe(@PathVariable Long id) {
        topicService.unsubscribe(id);
        return ResponseEntity.noContent().build();
    }
}
