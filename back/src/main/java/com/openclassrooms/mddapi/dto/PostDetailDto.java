package com.openclassrooms.mddapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO détaillé d'un article : toutes ses informations
 * ainsi que la liste de ses commentaires.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailDto {
    private Long id;
    private String title;
    private String content;
    private String author;
    private String topicTitle;
    private LocalDateTime createdAt;
    private List<CommentDto> comments;
}
