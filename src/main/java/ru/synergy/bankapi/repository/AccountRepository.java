package ru.synergy.bankapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.synergy.bankapi.entity.AccountEntity;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    AccountEntity findByUserId(Long userId);
}
