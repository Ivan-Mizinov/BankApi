package ru.synergy.bankapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.synergy.bankapi.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByLogin(String login);
}
