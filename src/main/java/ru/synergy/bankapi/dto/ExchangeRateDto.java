package ru.synergy.bankapi.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ExchangeRateDto {
    private String currency;
    private BigDecimal rate;
    private LocalDate date;
}
