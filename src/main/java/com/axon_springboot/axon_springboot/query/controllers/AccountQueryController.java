package com.axon_springboot.axon_springboot.query.controllers;

import com.axon_springboot.axon_springboot.commonapi.queries.GetAccountQuery;
import com.axon_springboot.axon_springboot.commonapi.queries.GetAllAccountsQuery;
import com.axon_springboot.axon_springboot.query.entities.Account;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/query/accounts")
@Slf4j
@AllArgsConstructor
public class AccountQueryController {
    private final QueryGateway queryGateway;

    @GetMapping("/all")
    public List<Account>  accountList(){
        return queryGateway.query(new GetAllAccountsQuery(), ResponseTypes.multipleInstancesOf(Account.class)).join();
    }

    @GetMapping("/{id}")
    public Account getAccount(@PathVariable String id){
        return queryGateway.query(new GetAccountQuery(id), ResponseTypes.instanceOf(Account.class)).join();
        }
}
