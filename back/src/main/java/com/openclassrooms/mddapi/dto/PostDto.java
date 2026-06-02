package com.openclassrooms.mddapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO résumé d'un article, utilisé pour l'affichage du fil d'actualité.
 * Ne contient pas les commentaires (voir {@link PostDetailDto}).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private String author;
    private String topicTitle;
    private LocalDateTime createdAt;
}
