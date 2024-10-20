package com.axon_springboot.axon_springboot.commonapi.events;

import com.axon_springboot.axon_springboot.commonapi.commands.enums.AccountStatus;
import lombok.Getter;

@Getter
public class AccountActivatedEvent extends BaseEvent <String>{
    public AccountStatus accountStatus;

    public AccountActivatedEvent(String id, AccountStatus status) {
        super(id);
        this.accountStatus = status;
    }
}
