package com.axon_springboot.axon_springboot.commonapi.events;

import com.axon_springboot.axon_springboot.commonapi.commands.enums.AccountStatus;
import lombok.Getter;

public class AccountActivatedEvent extends BaseEvent <String>{
    @Getter
    public AccountStatus accountStatus;

    public AccountActivatedEvent(String id, AccountStatus status) {
        super(id);
        this.accountStatus = status;
    }
}
