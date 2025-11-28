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

    public boolean changePassword(String login, String oldPassword, String newPassword) {
        if (oldPassword == null || newPassword == null ||
                oldPassword.length() < 8 || oldPassword.length() > 20 ||
                newPassword.length() < 8 || newPassword.length() > 20) {
            return false;
        }

        Optional<UserEntity> userOptional = userRepository.findByLogin(login);

        if (userOptional.isEmpty()) {
            return false;
        }

        UserEntity user = userOptional.get();

        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            return false;
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return true;
    }

    public Optional<UserEntity> authenticate(String login, String rawPassword) {
        return userRepository.findByLogin(login)
                .filter(user -> passwordEncoder.matches(rawPassword, user.getPasswordHash()));
    }
}
