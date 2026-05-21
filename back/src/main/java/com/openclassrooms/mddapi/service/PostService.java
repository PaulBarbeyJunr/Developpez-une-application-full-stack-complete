package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.CommentDto;
import com.openclassrooms.mddapi.dto.CreatePostRequest;
import com.openclassrooms.mddapi.dto.PostDetailDto;
import com.openclassrooms.mddapi.dto.PostDto;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service métier de gestion des articles :
 * consultation du fil d'actualité, consultation détaillée et création.
 */
@Service
@RequiredArgsConstructor
public class PostService {

    private static final String CREATED_AT = "createdAt";

    private final PostRepository postRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    /**
     * Construit le fil d'actualité de l'utilisateur courant :
     * les articles des thèmes auxquels il est abonné, triés par date.
     *
     * @param ascending {@code true} pour un tri du plus ancien au plus récent,
     *                  {@code false} pour du plus récent au plus ancien
     * @return la liste des articles du fil, triés
     */
    @Transactional(readOnly = true)
    public List<PostDto> getFeed(boolean ascending) {
        User user = loadCurrentUser();
        Set<Long> topicIds = user.getSubscriptions().stream()
                .map(Topic::getId)
                .collect(Collectors.toSet());

        if (topicIds.isEmpty()) {
            return Collections.emptyList();
        }

        Sort sort = ascending
                ? Sort.by(CREATED_AT).ascending()
                : Sort.by(CREATED_AT).descending();

        return postRepository.findByTopic_IdIn(topicIds, sort).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Renvoie le détail d'un article, commentaires inclus.
     *
     * @param postId identifiant de l'article
     * @return le {@link PostDetailDto} correspondant
     * @throws EntityNotFoundException si l'article n'existe pas
     */
    @Transactional(readOnly = true)
    public PostDetailDto getById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Article introuvable"));
        return toDetailDto(post);
    }

    /**
     * Crée un article pour l'utilisateur courant.
     * L'auteur et la date de création sont déterminés automatiquement.
     *
     * @param request les données de l'article (thème, titre, contenu)
     * @return le détail de l'article créé
     * @throws EntityNotFoundException si le thème indiqué n'existe pas
     */
    @Transactional
    public PostDetailDto create(CreatePostRequest request) {
        User author = loadCurrentUser();
        Topic topic = topicRepository.findById(request.getTopicId())
                .orElseThrow(() -> new EntityNotFoundException("Thème introuvable"));

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(author)
                .topic(topic)
                .build();

        Post saved = postRepository.save(post);
        return toDetailDto(saved);
    }

    private User loadCurrentUser() {
        Long id = SecurityUtils.getCurrentUserId();
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));
    }

    private PostDto toDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getAuthor().getUsername())
                .topicTitle(post.getTopic().getTitle())
                .createdAt(post.getCreatedAt())
                .build();
    }

    private PostDetailDto toDetailDto(Post post) {
        List<CommentDto> comments = post.getComments().stream()
                .map(comment -> CommentDto.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .author(comment.getAuthor().getUsername())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return PostDetailDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getAuthor().getUsername())
                .topicTitle(post.getTopic().getTitle())
                .createdAt(post.getCreatedAt())
                .comments(comments)
                .build();
    }
}
