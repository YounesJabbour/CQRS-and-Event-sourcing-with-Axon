package com.axon_springboot.axon_springboot.commonapi.events;

import com.axon_springboot.axon_springboot.commonapi.commands.enums.AccountStatus;
import lombok.Getter;

public class AccountCreditedEvent extends BaseEvent <String>{
    @Getter private double amount;
    @Getter private String currency;

    public AccountCreditedEvent(String id, double amount, String currency) {
        super(id);
        this.amount = amount;
        this.currency = currency;
    }

}
