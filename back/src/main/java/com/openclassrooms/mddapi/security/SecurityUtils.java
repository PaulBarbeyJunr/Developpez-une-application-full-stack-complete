package com.openclassrooms.mddapi.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utilitaires statiques pour accéder à l'utilisateur authentifié
 * depuis le {@link SecurityContextHolder}.
 */
public final class SecurityUtils {

    private SecurityUtils() {
        // utility class
    }

    /**
     * Récupère les détails de l'utilisateur authentifié pour la requête courante.
     *
     * @return le {@link UserDetailsImpl} de l'utilisateur courant
     * @throws IllegalStateException si aucun utilisateur n'est authentifié
     */
    public static UserDetailsImpl getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            throw new IllegalStateException("Aucun utilisateur authentifié");
        }
        return (UserDetailsImpl) authentication.getPrincipal();
    }

    /**
     * Récupère l'identifiant numérique de l'utilisateur authentifié.
     *
     * @return l'id de l'utilisateur courant
     */
    public static Long getCurrentUserId() {
        return getCurrentUserDetails().getId();
    }
}
