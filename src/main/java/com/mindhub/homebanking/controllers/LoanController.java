package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.dtos.LoanApplicationDTO.*;
import static com.mindhub.homebanking.models.TransactionType.CREDITO;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    LoanService loanService;

    @GetMapping("/loans")
    public List<LoanDTO> getLoanApplicationDTO() {
        return loanService.getLoanApplicationDTO();
    }

    @Transactional
    //@RequestMapping(value = "/api/loans", method = RequestMethod.POST)
    @PostMapping("/api/loans")
    public ResponseEntity<Object> applyloan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {
        //LoanApplicationDTO loanRequest = null;
        if (loanApplicationDTO.getAmount() <= 0 || loanApplicationDTO.getPayments() <= 0
                || loanApplicationDTO.toAccountNumber() == null) {
            return new ResponseEntity<>("Los datos de la solicitud son incorrectos.", HttpStatus.FORBIDDEN);
        }

        Loan loan = loanRepository.findById(loanApplicationDTO.getLoanId()).orElse(null);

        if (loan == null) {
            return new ResponseEntity<>("El préstamo no existe.", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("El monto solicitado excede el monto máximo del préstamo.", HttpStatus.FORBIDDEN);
        }

        //cant cuotas
        if (!loan.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("La cantidad de cuotas solicitadas no está disponible para este préstamo.", HttpStatus.FORBIDDEN);
        }

        Account destinationAccount = accountRepository.findByNumber(loanApplicationDTO.toAccountNumber());
        if (destinationAccount == null) {
            return new ResponseEntity<>("La cuenta de destino no existe.", HttpStatus.FORBIDDEN);
        }

        Client client = clientRepository.findByEmail(authentication.getName());
        if (!client.getAccounts().contains(destinationAccount)){
            return new ResponseEntity<>("La cuenta de destino no pertenece a un cliente autenticado", HttpStatus.FORBIDDEN);
        }
        double amountToAccredit = (loanApplicationDTO.getAmount() * 1.20);

        Transaction transaction = new Transaction(loanApplicationDTO.getAmount(), loan.getName() + "loan aproved", LocalDateTime.now(), CREDITO);


        // Guardar la transacción de crédito
        transactionRepository.save(transaction);

        // Actualizar saldo destino
        destinationAccount.setBalance(destinationAccount.getBalance() + loanApplicationDTO.getAmount());
        accountRepository.save(destinationAccount);

        return new ResponseEntity<>("Préstamo aprobado y acreditado en la cuenta de destino.", HttpStatus.CREATED);
    }
}
