package com.openclassrooms.mddapi.security;

import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service de chargement des utilisateurs pour Spring Security.
 * Recherche un utilisateur par e-mail OU nom d'utilisateur, conformément
 * à la spécification fonctionnelle qui autorise les deux pour la connexion.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Charge l'utilisateur par e-mail ou par nom d'utilisateur.
     *
     * @param identifier valeur fournie lors de la connexion (email ou username)
     * @return les détails de l'utilisateur encapsulés dans {@link UserDetailsImpl}
     * @throws UsernameNotFoundException si aucun utilisateur ne correspond
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        User user = userRepository.findByEmailOrUsername(identifier, identifier)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Utilisateur introuvable avec l'identifiant : " + identifier));
        return UserDetailsImpl.build(user);
    }
}
