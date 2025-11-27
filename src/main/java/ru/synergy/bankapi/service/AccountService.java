package ru.synergy.bankapi.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.synergy.bankapi.entity.AccountEntity;
import ru.synergy.bankapi.entity.UserEntity;
import ru.synergy.bankapi.repository.AccountRepository;
import ru.synergy.bankapi.repository.UserRepository;

import java.util.Objects;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public UserEntity getCurrentUser() {
        String login = getCurrentUsername();
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    public AccountEntity getUserAccount() {
        UserEntity user = getCurrentUser();
        return accountRepository.findByUserId(user.getId());
    }

    private String getCurrentUsername() {
        return Objects.requireNonNull(SecurityContextHolder
                        .getContext()
                        .getAuthentication())
                .getName();
    }
}
