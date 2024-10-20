package com.axon_springboot.axon_springboot.query.service;
import com.axon_springboot.axon_springboot.commonapi.commands.enums.OperationStatus;
import com.axon_springboot.axon_springboot.commonapi.events.AccountActivatedEvent;
import com.axon_springboot.axon_springboot.commonapi.events.AccountCreatedEvent;
import com.axon_springboot.axon_springboot.commonapi.events.AccountCreditedEvent;
import com.axon_springboot.axon_springboot.commonapi.events.AccountDebitedEvent;
import com.axon_springboot.axon_springboot.commonapi.queries.GetAccountQuery;
import com.axon_springboot.axon_springboot.commonapi.queries.GetAllAccountsQuery;
import com.axon_springboot.axon_springboot.query.entities.Account;
import com.axon_springboot.axon_springboot.query.entities.Operation;
import com.axon_springboot.axon_springboot.query.repository.AccountRepository;
import com.axon_springboot.axon_springboot.query.repository.OperationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;


@Transactional
@AllArgsConstructor
@Slf4j
@Service
public class AccountServiceHandler {
    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;
    private final QueryUpdateEmitter queryUpdateEmitter;

    // save the account in the database when an AccountCreatedEvent is received.
    @EventHandler
    public void on(AccountCreatedEvent event) {
        log.info("****************************");
        log.info("Account created event received");
        log.info("****************************");
        accountRepository.save(
                Account.builder()
                        .id(event.getId())
                        .balance(event.getInitialBalance())
                        .currency(event.getCurrency())
                        .status(event.getStatus())
                        .build()
        );
    }

    @EventHandler
    public void on (AccountActivatedEvent event) {
        log.info("****************************");
        log.info("Account activated event received");
        log.info("****************************");

        accountRepository.findById(event.getId()).ifPresent(account -> {
            account.setStatus(event.getAccountStatus());
            accountRepository.save(account);
        });
    }

    @EventHandler
    public void on(AccountDebitedEvent event) {
        log.info("****************************");
        log.info("Account debited event received");
        log.info("****************************");

       Account account =  accountRepository.findById(event.getId()).orElseThrow(() -> new IllegalArgumentException("Account not found"));
       account.setBalance(account.getBalance() - event.getAmount());
       Account savedAccount = accountRepository.save(account);

       Operation operation = Operation.builder()
                .account(savedAccount)
                .status(OperationStatus.DEBIT)
                .createdAt(new Date())
                .build();
       operationRepository.save(operation);
//       queryUpdateEmitter.emit();
    }

    @EventHandler
    public void on(AccountCreditedEvent event) {
        log.info("****************************");
        log.info("Account debited event received");
        log.info("****************************");

        Account account =  accountRepository.findById(event.getId()).orElseThrow(() -> new IllegalArgumentException("Account not found"));
        account.setBalance(account.getBalance() + event.getAmount());
            Account savedAccount = accountRepository.save(account);

        Operation operation = Operation.builder()
                .account(savedAccount)
                .status(OperationStatus.CREDIT)
                .createdAt(new Date())
                .build();
        operationRepository.save(operation);
    }

    @QueryHandler
    public List<Account> on(GetAllAccountsQuery query) {
    return accountRepository.findAll();
    }

    @QueryHandler
    public Account on(GetAccountQuery query) {
        return accountRepository.findById(query.getId()).orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }
}

