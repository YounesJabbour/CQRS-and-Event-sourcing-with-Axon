package com.axon_springboot.axon_springboot.commands.controller;
import com.axon_springboot.axon_springboot.commonapi.commands.CreateAccountCommand;
import com.axon_springboot.axon_springboot.commonapi.commands.CreditAccountCommand;
import com.axon_springboot.axon_springboot.commonapi.commands.DebitAccountCommand;
import com.axon_springboot.axon_springboot.commonapi.dtos.CreateAccountRequestDTO;
import com.axon_springboot.axon_springboot.commonapi.dtos.CreditAccountRequestDto;
import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;


@RestController
@RequestMapping(value = "/commands/accounts")
@AllArgsConstructor
public class AccountCommandController {

    private CommandGateway commandGateway;
    private EventStore eventStore;


    @PostMapping(path = "/create")
    public CompletableFuture<String> createAccount(@RequestBody CreateAccountRequestDTO request) {
        return commandGateway.send(new CreateAccountCommand(
                UUID.randomUUID().toString(),
                request.getInitialBalance(),
                request.getCurrency()
        ));
    }

    @GetMapping("/eventStore/{accountId}")
    public Stream eventStore(@PathVariable String accountId) {
        return eventStore.readEvents(accountId).asStream();
    }

    @PutMapping(path="/credit")
    public CompletableFuture<String> creditMoneyToAccount(@RequestBody CreditAccountRequestDto request) {
        return commandGateway.send(new CreditAccountCommand(
                request.getAccountId(),
                request.getAmount(),
                request.getCurrency()
        ));
    }

    @PutMapping(path="/debit")
    public CompletableFuture<String> debitMoneyFromAccount(@RequestBody CreditAccountRequestDto request) {
        return commandGateway.send(new DebitAccountCommand(
                request.getAccountId(),
                request.getAmount(),
                request.getCurrency()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception exception) {
        return new ResponseEntity<>(
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}