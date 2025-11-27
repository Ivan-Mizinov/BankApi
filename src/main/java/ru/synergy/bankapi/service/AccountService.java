package ru.synergy.bankapi.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.synergy.bankapi.entity.AccountEntity;
import ru.synergy.bankapi.entity.TransactionEntity;
import ru.synergy.bankapi.entity.TransactionType;
import ru.synergy.bankapi.entity.UserEntity;
import ru.synergy.bankapi.exception.InsufficientFundsException;
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
        validateAmount(amount, "пополнения");

        AccountEntity account = fetchUserAccount();

        account.setBalance(account.getBalance().add(amount));

        TransactionEntity transaction = createTransaction(
                account, amount, "Пополнение счёта", TransactionType.DEPOSIT
        );

        account.getTransactions().add(transaction);

        accountRepository.save(account);
        transactionRepository.save(transaction);
    }

    @Transactional
    public void withdrawFromAccount(BigDecimal amount) {
        validateAmount(amount, "снятия");

        AccountEntity account = fetchUserAccount();

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(
                    "Недостаточно средств на счёте. " +
                    "Текущий баланс: " + account.getBalance() +
                    ", запрашиваемая сумма: " + amount
            );
        }

        account.setBalance(account.getBalance().subtract(amount));

        TransactionEntity transaction = createTransaction(
                account, amount, "Снятие со счёта", TransactionType.WITHDRAWAL
        );

        account.getTransactions().add(transaction);

        accountRepository.save(account);
        transactionRepository.save(transaction);
    }

    @Transactional
    public void transferToUser(String recipientLogin, BigDecimal amount) {
        validateAmount(amount, "перевода");

        AccountEntity senderAccount = fetchUserAccount();
        UserEntity sender = getCurrentUser();

        if (senderAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(
                    "Недостаточно средств на счёте. " +
                    "Текущий баланс: " + senderAccount.getBalance() +
                    ", запрашиваемая сумма: " + amount
            );
        }

        UserEntity recipient = userRepository.findByLogin(recipientLogin)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с логином " + recipientLogin + " не найден"));
        AccountEntity recipientAccount = accountRepository.findByUserId(recipient.getId());
        if (recipientAccount == null) {
            throw new IllegalArgumentException("Счёт получателя не найден");
        }

        if (sender.getId().equals(recipient.getId())) {
            throw new IllegalArgumentException("Нельзя перевести средства самому себе");
        }

        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
        recipientAccount.setBalance(recipientAccount.getBalance().add(amount));

        TransactionEntity senderTransaction = createTransaction(
                senderAccount,
                amount,
                "Перевод пользователю " + recipientLogin,
                TransactionType.TRANSFER_OUT
        );
        TransactionEntity recipientTransaction = createTransaction(
                recipientAccount,
                amount,
                "Перевод от пользователя " + sender.getLogin(),
                TransactionType.TRANSFER_IN
        );

        senderAccount.getTransactions().add(senderTransaction);
        recipientAccount.getTransactions().add(recipientTransaction);

        accountRepository.save(senderAccount);
        accountRepository.save(recipientAccount);
        transactionRepository.save(senderTransaction);
        transactionRepository.save(recipientTransaction);
    }

    private void validateAmount(BigDecimal amount, String operation) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма " + operation + " должна быть больше нуля");
        }
    }

    private AccountEntity fetchUserAccount() {
        AccountEntity account = getUserAccount();
        if (account == null) {
            throw new IllegalArgumentException("Счёт пользователя не найден");
        }
        return account;
    }

    private TransactionEntity createTransaction(
            AccountEntity account,
            BigDecimal amount,
            String description,
            TransactionType type
    ) {
        TransactionEntity transaction = new TransactionEntity();
        transaction.setDate(LocalDateTime.now());
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setTransactionType(type);
        transaction.setAccount(account);
        return transaction;
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
