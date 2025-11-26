package ru.synergy.bankapi.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.synergy.bankapi.entity.UserEntity;
import ru.synergy.bankapi.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<UserEntity> authenticate(String login, String rawPassword) {
        return userRepository.findByLogin(login)
                .filter(user -> passwordEncoder.matches(rawPassword, user.getPasswordHash()));
    }
}
