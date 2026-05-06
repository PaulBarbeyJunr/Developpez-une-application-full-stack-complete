package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository Spring Data JPA pour l'entité {@link User}.
 * Fournit les opérations CRUD standard ainsi que des recherches
 * par email et par nom d'utilisateur.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Recherche un utilisateur par son adresse e-mail.
     *
     * @param email l'adresse e-mail à rechercher
     * @return un {@link Optional} contenant l'utilisateur trouvé, ou vide sinon
     */
    Optional<User> findByEmail(String email);

    /**
     * Recherche un utilisateur par son nom d'utilisateur.
     *
     * @param username le nom d'utilisateur à rechercher
     * @return un {@link Optional} contenant l'utilisateur trouvé, ou vide sinon
     */
    Optional<User> findByUsername(String username);

    /**
     * Recherche un utilisateur dont l'email OU le nom d'utilisateur correspond à l'identifiant fourni.
     * Utilisé pour la connexion qui accepte indifféremment l'un ou l'autre.
     *
     * @param identifier valeur à comparer à l'email et au nom d'utilisateur
     * @return un {@link Optional} contenant l'utilisateur trouvé, ou vide sinon
     */
    Optional<User> findByEmailOrUsername(String email, String username);

    /**
     * Indique si un utilisateur existe avec l'email donné.
     *
     * @param email l'adresse e-mail à tester
     * @return {@code true} si un utilisateur existe, {@code false} sinon
     */
    boolean existsByEmail(String email);

    /**
     * Indique si un utilisateur existe avec le nom d'utilisateur donné.
     *
     * @param username le nom d'utilisateur à tester
     * @return {@code true} si un utilisateur existe, {@code false} sinon
     */
    boolean existsByUsername(String username);
}
