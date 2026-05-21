package com.openclassrooms.mddapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Données de création d'un commentaire.
 * L'auteur et la date sont déterminés automatiquement côté serveur,
 * et le commentaire est rattaché à l'article ciblé par l'URL.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentRequest {

    @NotBlank(message = "Le contenu du commentaire est obligatoire")
    private String content;
}
