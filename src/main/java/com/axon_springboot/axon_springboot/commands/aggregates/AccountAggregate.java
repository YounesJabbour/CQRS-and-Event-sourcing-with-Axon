package com.axon_springboot.axon_springboot.commands.aggregates;


import com.axon_springboot.axon_springboot.commonapi.commands.CreateAccountCommand;
import com.axon_springboot.axon_springboot.commonapi.commands.enums.AccountStatus;
import com.axon_springboot.axon_springboot.commonapi.events.AccountActivatedEvent;
import com.axon_springboot.axon_springboot.commonapi.events.AccountCreatedEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;


@NoArgsConstructor
@Aggregate
public class AccountAggregate {

    @AggregateIdentifier
    private String accountId;
    private double balance;
    private String currency;
    private AccountStatus status;

    @CommandHandler
    public AccountAggregate(CreateAccountCommand command) {
        if (command.getInitialBalance() < 0) {
            throw new IllegalArgumentException("Initial balance cannot be less than 0");
        }
        AggregateLifecycle.apply(
        new AccountCreatedEvent(
                command.getId(),
                command.getInitialBalance(),
                command.getCurrency()
        ));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        this.accountId = event.getId();
        this.balance = event.getInitialBalance();
        this.currency = event.getCurrency();
        this.status = AccountStatus.CREATED;
    AggregateLifecycle.apply(new AccountActivatedEvent(event.getId(), status.ACTIVATED));
    }

    @EventSourcingHandler
    public void on(AccountActivatedEvent event) {
        this.status = event.getAccountStatus();
    }
}