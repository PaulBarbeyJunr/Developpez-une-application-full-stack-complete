package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Repository Spring Data JPA pour l'entité {@link Post}.
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * Récupère les articles appartenant aux thèmes fournis,
     * en appliquant le tri demandé sur la date de création.
     * Utilisé pour construire le fil d'actualité d'un utilisateur
     * à partir de ses abonnements.
     *
     * @param topicIds identifiants des thèmes suivis par l'utilisateur
     * @param sort     critère de tri (généralement sur {@code createdAt})
     * @return la liste des articles correspondants, triés
     */
    List<Post> findByTopic_IdIn(Collection<Long> topicIds, Sort sort);
}
