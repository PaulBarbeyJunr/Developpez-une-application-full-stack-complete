package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.CommentDto;
import com.openclassrooms.mddapi.dto.CreateCommentRequest;
import com.openclassrooms.mddapi.dto.CreatePostRequest;
import com.openclassrooms.mddapi.dto.PostDetailDto;
import com.openclassrooms.mddapi.dto.PostDto;
import com.openclassrooms.mddapi.service.CommentService;
import com.openclassrooms.mddapi.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Contrôleur REST pour la gestion des articles :
 * fil d'actualité, consultation détaillée, création et ajout de commentaires.
 */
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    /**
     * Renvoie le fil d'actualité de l'utilisateur courant.
     *
     * @param sort {@code "asc"} pour le plus ancien au plus récent,
     *             toute autre valeur (défaut {@code "desc"}) pour l'inverse
     */
    @GetMapping
    public ResponseEntity<List<PostDto>> getFeed(
            @RequestParam(defaultValue = "desc") String sort) {
        boolean ascending = "asc".equalsIgnoreCase(sort);
        return ResponseEntity.ok(postService.getFeed(ascending));
    }

    /**
     * Renvoie le détail d'un article (commentaires inclus).
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostDetailDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getById(id));
    }

    /**
     * Crée un nouvel article pour l'utilisateur courant.
     */
    @PostMapping
    public ResponseEntity<PostDetailDto> create(
            @Valid @RequestBody CreatePostRequest request) {
        return ResponseEntity.ok(postService.create(request));
    }

    /**
     * Ajoute un commentaire à l'article indiqué.
     */
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDto> addComment(
            @PathVariable Long id,
            @Valid @RequestBody CreateCommentRequest request) {
        return ResponseEntity.ok(commentService.create(id, request));
    }
}
