package com.axon_springboot.axon_springboot.commonapi.events;

import com.axon_springboot.axon_springboot.commonapi.commands.enums.AccountStatus;
import lombok.Getter;

@Getter
public class AccountCreatedEvent extends BaseEvent <String>{
    public final String currency;
    public final double initialBalance;
    public AccountStatus status;

    public AccountCreatedEvent(String id, double accountBalance, String currency, AccountStatus status) {
        super(id);
        this.initialBalance = accountBalance;
        this.currency = currency;
        this.status = status;
    }
}
