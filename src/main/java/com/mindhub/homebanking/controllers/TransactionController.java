package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.models.TransactionType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    CardRepository cardRepository;

    @Transactional
    @RequestMapping(path = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> createTransaction(
            @RequestParam("amount") double amount,
            @RequestParam("description") String description,
            @RequestParam("fromAccountNumber") String accountFromNumber,
            @RequestParam("toAccountNumber") String destinationAccountNumber,
            Authentication authentication) {


        if ( Double.isNaN(amount) || description.isEmpty()
                || accountFromNumber.isEmpty() || destinationAccountNumber.isEmpty()) {
            return ResponseEntity.badRequest().body("Los campos no pueden estar vacíos.");
        }

        if (amount <= 0) {
            return new ResponseEntity<>("El monto debe ser superior a 0.", HttpStatus.BAD_REQUEST);
        }

        if (accountFromNumber.equals(destinationAccountNumber)) {
            return new ResponseEntity<>("Los números de las cuentas de origen y de destino no pueden ser iguales.", HttpStatus.BAD_REQUEST);
        }




        Client client = clientRepository.findByEmail(authentication.getName());

        Account sourceAccount = accountRepository.findByNumber(accountFromNumber);

        if(sourceAccount == null){
            return new ResponseEntity<>("Account/s not found", HttpStatus.FORBIDDEN);
        }

        Client authenticatedClient = sourceAccount.getClient();
        String authenticadedUserEmail = authentication.getName();
        if(!authenticatedClient.getEmail().equals(authenticadedUserEmail)){
            return new ResponseEntity<>("Unauthorized",HttpStatus.UNAUTHORIZED);   // client log
        }

        Account destinationAccount = accountRepository.findByNumber(destinationAccountNumber);
        if (destinationAccount == null) {
            return ResponseEntity.badRequest().body("La cuenta de destino no existe.");
        }

        if (sourceAccount.getBalance() < amount) {
            return ResponseEntity.badRequest().body("Saldo insuficiente en la cuenta de origen.");
        }


        Transaction debitTransaction = new Transaction(-amount, description + " DEBITO - " + accountFromNumber,  LocalDateTime.now(), TransactionType.DEBITO);
        Transaction creditTransaction = new Transaction(amount, description + " CREDITO - " + destinationAccountNumber, LocalDateTime.now(), TransactionType.CREDITO);

        sourceAccount.addTransaction(debitTransaction);
        destinationAccount.addTransaction(creditTransaction);

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);

        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        return ResponseEntity.ok("Transaccion exitosa.");
    }
}

