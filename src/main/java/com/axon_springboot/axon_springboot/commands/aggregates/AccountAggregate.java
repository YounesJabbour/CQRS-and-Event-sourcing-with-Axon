package com.axon_springboot.axon_springboot.commands.aggregates;


import com.axon_springboot.axon_springboot.commonapi.commands.CreateAccountCommand;
import com.axon_springboot.axon_springboot.commonapi.commands.CreditAccountCommand;
import com.axon_springboot.axon_springboot.commonapi.commands.DebitAccountCommand;
import com.axon_springboot.axon_springboot.commonapi.commands.enums.AccountStatus;
import com.axon_springboot.axon_springboot.commonapi.events.AccountActivatedEvent;
import com.axon_springboot.axon_springboot.commonapi.events.AccountCreatedEvent;
import com.axon_springboot.axon_springboot.commonapi.events.AccountCreditedEvent;
import com.axon_springboot.axon_springboot.commonapi.events.AccountDebitedEvent;
import com.axon_springboot.axon_springboot.commonapi.exceptions.AmountNegativeException;
import com.axon_springboot.axon_springboot.commonapi.exceptions.InsufficientBalanceException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;


@NoArgsConstructor
@Aggregate
@Slf4j
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
                command.getCurrency(),
                AccountStatus.CREATED
        ));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        this.accountId = event.getId();
        this.balance = event.getInitialBalance();
        this.currency = event.getCurrency();
        this.status = AccountStatus.CREATED;
    AggregateLifecycle.apply(new AccountActivatedEvent(event.getId(), AccountStatus.ACTIVATED));
    }

    @EventSourcingHandler
    public void on(AccountActivatedEvent event) {
        log.info("AccountActivatedEvent Occurred");
        this.status = event.getAccountStatus();
    }

    @CommandHandler
    public void handle(CreditAccountCommand command) {
        if (command.getAmount() <= 0) {
            throw new AmountNegativeException("Credit amount should be greater than 0");
        }
        // Emettre événement
        AggregateLifecycle.apply(new AccountCreditedEvent(command.getId(), command.getAmount(), command.getCurrency()));
    }

    @CommandHandler
    public void handle(DebitAccountCommand command) {
        if(this.status != AccountStatus.ACTIVATED) {
            throw new IllegalStateException("Account not activated");
        }
        if (command.getAmount() <= 0) {
            throw new AmountNegativeException("Debit amount should be greater than =>"+command.getAmount());
        }
        if (this.balance - command.getAmount() < 0) {
            throw new InsufficientBalanceException("Insufficient balance =>"+this.balance);
        }
        // Emettre événement
        AggregateLifecycle.apply(new AccountDebitedEvent(command.getId(), command.getAmount(), command.getCurrency()));
    }

    @EventSourcingHandler
    public void on(AccountDebitedEvent event) {
        this.balance -= event.getAmount();
    }

    @EventSourcingHandler
    public void on(AccountCreditedEvent event) {
        this.balance += event.getAmount();
    }

}