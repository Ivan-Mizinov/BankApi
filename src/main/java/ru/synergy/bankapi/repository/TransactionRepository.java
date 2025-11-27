package ru.synergy.bankapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.synergy.bankapi.entity.AccountEntity;
import ru.synergy.bankapi.entity.TransactionEntity;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findFirst10ByAccountOrderByDateDesc(AccountEntity account);
}
