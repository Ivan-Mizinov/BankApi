package ru.synergy.bankapi.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.synergy.bankapi.entity.Role;
import ru.synergy.bankapi.entity.UserEntity;
import ru.synergy.bankapi.repository.UserRepository;

@Configuration
public class DataInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    CommandLineRunner initUsers() {
        return args -> {
            if (userRepository.findByLogin("user").isEmpty()) {
                UserEntity user = new UserEntity();
                user.setLogin("user");
                user.setPasswordHash(passwordEncoder.encode("password123"));
                user.setRole(Role.USER);
                userRepository.save(user);
            }

            if (userRepository.findByLogin("admin").isEmpty()) {
                UserEntity admin = new UserEntity();
                admin.setLogin("admin");
                admin.setPasswordHash(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
            }
        };
    }
}
