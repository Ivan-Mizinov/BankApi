package ru.synergy.bankapi.entity;


import lombok.Getter;

@Getter
public enum AccountType {
    CURRENT("Текущий"),
    SAVINGS("Накопительный"),
    DEPOSIT("Депозитный"),
    CREDIT("Кредитный");

    private final String displayName;

    AccountType(String displayName) {
        this.displayName = displayName;
    }
}
