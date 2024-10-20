package com.axon_springboot.axon_springboot.commonapi.dtos;

import lombok.Getter;

@Getter
public class CreditAccountRequestDto {
    private String accountId; // the id of the account to credit
    private double amount;
    private String currency;

    public CreditAccountRequestDto(double amount, String currency, String accountId) {
        this.amount = amount;
        this.currency = currency;
        this.accountId = accountId;
    }
}
