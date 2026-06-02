package com.openclassrooms.mddapi.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Données de connexion d'un utilisateur.
 * Le champ {@code identifier} accepte indifféremment l'email
 * ou le nom d'utilisateur, conformément aux spécifications.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "L'identifiant (email ou nom d'utilisateur) est obligatoire")
    private String identifier;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
}
