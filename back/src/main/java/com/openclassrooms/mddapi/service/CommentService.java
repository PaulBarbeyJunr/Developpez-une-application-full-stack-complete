package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.CommentDto;
import com.openclassrooms.mddapi.dto.CreateCommentRequest;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

/**
 * Service métier de gestion des commentaires.
 * Un commentaire est toujours rattaché à un article et n'est pas récursif.
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * Ajoute un commentaire à un article pour l'utilisateur courant.
     * L'auteur et la date de création sont déterminés automatiquement.
     *
     * @param postId  identifiant de l'article commenté
     * @param request le contenu du commentaire
     * @return le commentaire créé sous forme de {@link CommentDto}
     * @throws EntityNotFoundException si l'article n'existe pas
     */
    @Transactional
    public CommentDto create(Long postId, CreateCommentRequest request) {
        User author = loadCurrentUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Article introuvable"));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .author(author)
                .post(post)
                .build();

        Comment saved = commentRepository.save(comment);

        return CommentDto.builder()
                .id(saved.getId())
                .content(saved.getContent())
                .author(saved.getAuthor().getUsername())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    private User loadCurrentUser() {
        Long id = SecurityUtils.getCurrentUserId();
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));
    }
}
