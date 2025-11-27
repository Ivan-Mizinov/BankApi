package ru.synergy.bankapi.entity;

import lombok.Getter;

@Getter
public enum TransactionType {
    DEPOSIT("Внесение наличных"),
    WITHDRAWAL("Снятие наличных"),
    TRANSFER_OUT("Перевод"),
    TRANSFER_IN("Поступление перевода");

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }
}
