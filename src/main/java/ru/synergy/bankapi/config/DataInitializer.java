package ru.synergy.bankapi.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.synergy.bankapi.entity.AccountEntity;
import ru.synergy.bankapi.entity.AccountType;
import ru.synergy.bankapi.entity.Role;
import ru.synergy.bankapi.entity.UserEntity;
import ru.synergy.bankapi.repository.AccountRepository;
import ru.synergy.bankapi.repository.UserRepository;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           AccountRepository accountRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    CommandLineRunner initUsers() {
        return args -> {
            if (userRepository.findByLogin("admin").isEmpty()) {
                UserEntity admin = new UserEntity();
                admin.setLogin("admin");
                admin.setPasswordHash(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN);
                UserEntity savedAdmin = userRepository.save(admin);

                AccountEntity adminAccount = new AccountEntity();
                adminAccount.setAccountNumber("Acc-001");
                adminAccount.setAccountType(AccountType.SAVINGS);
                adminAccount.setBalance(new BigDecimal("100000.00"));
                adminAccount.setUser(savedAdmin);
                accountRepository.save(adminAccount);
            }

            if (userRepository.findByLogin("user1").isEmpty()) {
                UserEntity user = new UserEntity();
                user.setLogin("user1");
                user.setPasswordHash(passwordEncoder.encode("password1"));
                user.setRole(Role.USER);
                UserEntity savedUser = userRepository.save(user);

                AccountEntity userAccount = new AccountEntity();
                userAccount.setAccountNumber("Acc-002");
                userAccount.setAccountType(AccountType.SAVINGS);
                userAccount.setBalance(new BigDecimal("100000.00"));
                userAccount.setUser(savedUser);
                accountRepository.save(userAccount);
            }
        };
    }
}
