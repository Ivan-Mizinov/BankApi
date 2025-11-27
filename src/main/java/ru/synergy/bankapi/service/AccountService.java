package ru.synergy.bankapi.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.synergy.bankapi.entity.AccountEntity;
import ru.synergy.bankapi.entity.TransactionEntity;
import ru.synergy.bankapi.entity.TransactionType;
import ru.synergy.bankapi.entity.UserEntity;
import ru.synergy.bankapi.repository.AccountRepository;
import ru.synergy.bankapi.repository.TransactionRepository;
import ru.synergy.bankapi.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository,
                          UserRepository userRepository,
                          TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void depositToAccount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма пополнения должна быть больше нуля");
        }

        AccountEntity account = getUserAccount();

        if (account == null) {
            throw new IllegalArgumentException("Счёт пользователя не найден");
        }

        account.setBalance(account.getBalance().add(amount));

        TransactionEntity transaction = new TransactionEntity();
        transaction.setDate(LocalDateTime.now());
        transaction.setAmount(amount);
        transaction.setDescription("Пополнение счёта");
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setAccount(account);

        account.getTransactions().add(transaction);

        accountRepository.save(account);
        transactionRepository.save(transaction);
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

    public List<TransactionEntity> getLast10Transactions() {
        AccountEntity account = getUserAccount();
        return transactionRepository.findFirst10ByAccountOrderByDateDesc(account);
    }
}
