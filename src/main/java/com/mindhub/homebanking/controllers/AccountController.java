package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Util;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private Util util;

    // @JsonIgnore
    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){

        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }


    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){

        AccountDTO account = new AccountDTO(Objects.requireNonNull(accountRepository.findById(id).orElse(null)));
        return account;
    }

    @RequestMapping( path = "/clients/current/accounts", method = RequestMethod.POST)

    public ResponseEntity<Object> createAccount(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());

        if (client==null){
            return new ResponseEntity<>("Usuario no encontrado ", HttpStatus.FORBIDDEN);
        }

        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("Ha alcanzado el límite máximo de cuentas.", HttpStatus.FORBIDDEN);
        }

        String accountNumber = util.generateNumber();
        Account account = new Account("VIN " + accountNumber, LocalDate.now() , 0);

        client.addAccount(account);
        account.setClient(client);
        accountRepository.save(account);

        AccountDTO accountDTO = new AccountDTO(account);


        return new ResponseEntity<>(accountDTO, HttpStatus.CREATED);
    }









}
