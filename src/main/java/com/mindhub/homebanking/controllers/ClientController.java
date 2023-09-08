package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Util;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private Util util;


    @JsonIgnore
    @RequestMapping("/clients")
    public List<ClientDTO> getClients() {

        return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }

    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){

        ClientDTO client = new ClientDTO(clientRepository.findById(id).orElse(null));
        return client;
    }

    // @RequestMapping(path = "/clients", method = RequestMethod.POST)
    @PostMapping("/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (clientRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("El nombre de usuario ya está en uso", HttpStatus.FORBIDDEN);
        }

        Client clientNew = new Client(firstName, lastName, email, passwordEncoder.encode(password));

        String accountNumber = util.generateNumber();
        Account account = new Account(accountNumber, LocalDate.now(), 0);
        clientNew.addAccount(account);
        account.setClient(clientNew);

        clientRepository.save(clientNew);
        accountRepository.save(account);


        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @RequestMapping(value = "/clients/current", method = RequestMethod.GET)
    public ClientDTO getClientCurrent(Authentication authentication){
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }

    @RequestMapping(value = "/clients/current/accounts", method = RequestMethod.GET)
    public Set<AccountDTO> getAccountsClientCurrent(Authentication authentication){
       ClientDTO clientCurrent = new  ClientDTO(clientRepository.findByEmail(authentication.getName()));
       return clientCurrent.getAccounts();

    }
}

