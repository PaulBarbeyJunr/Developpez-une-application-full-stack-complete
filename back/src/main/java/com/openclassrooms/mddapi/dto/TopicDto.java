package com.openclassrooms.mddapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO représentant un thème.
 * Le champ {@code isSubscribed} indique si l'utilisateur courant
 * est abonné. Il est omis du JSON quand il n'est pas pertinent
 * (par exemple dans la liste d'abonnements du profil).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TopicDto {
    private Long id;
    private String title;
    private String description;
    private Boolean isSubscribed;
}
