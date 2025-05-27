package com.example.toytalk.domain.users.repository;

import com.example.toytalk.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("SELECT u FROM User u WHERE u.id = :userId AND u.status = 'ACTIVATED'")
    Optional<User> findActivatedById(UUID userId);

    @Query("SELECT u FROM User u WHERE u.username = :username AND u.status = 'ACTIVATED'")
    Optional<User> findActivatedByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVATED'")
    List<User> findAllActivated();

    Optional<Object> findByUsername(String username);

    Optional<Object> findByEmail(String email);

    Optional<User> findByOauthId(String oauthId);
}

