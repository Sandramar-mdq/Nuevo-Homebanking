package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.utils.CardUtils;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static com.mindhub.homebanking.utils.CardUtils.getCVV;
import static com.mindhub.homebanking.utils.CardUtils.getCardNumber;
import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private Util util;

    @JsonIgnore
    @RequestMapping("/cards")
    public List<CardDTO> getCards(){

        return cardRepository.findAll().stream().map(CardDTO::new).collect(toList());
    }

    @GetMapping("/cards/{id}")
    public CardDTO getCard(@PathVariable Long id){

        CardDTO card = new CardDTO(cardRepository.findById(id).orElse(null));
        return card;
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCards(@RequestParam CardColor cardColor,
                                              @RequestParam CardType cardType,
                                              Authentication authentication) {
        String userEmail = authentication.getName();
        Client client = clientRepository.findByEmail(userEmail);

        if (client != null) {
            long existingCardsCount = client.getCardsCLi().stream().filter(card -> card.getType() == cardType).count();
            boolean hasCardWithSameColor = client.getCardsCLi().stream().anyMatch(card -> card.getColor() == cardColor);

            if (hasCardWithSameColor) {
                return new ResponseEntity<>("Ya existe una tarjeta de ese color.", HttpStatus.FORBIDDEN);
            }
            else {
                Card card = new Card();
                card.setColor(cardColor);
                card.setType(cardType);
                card.setCardHolder(client.getFirstName() + " " + client.getLastName());
                card.setNumber(getCardNumber() );
                card.setFromDate(LocalDate.now());
                card.setThruDate(LocalDate.now().plusYears(5));
                card.setCvv(getCVV());

                client.addCard(card);
                cardRepository.save(card);
                clientRepository.save(client);

                return new ResponseEntity<>("Tarjeta creada", HttpStatus.CREATED);
            }
        } else {
            throw new UsernameNotFoundException("Usuario desconocido: " + userEmail);
        }
    }

    String cardNumber = getCardNumber();

    int cvv = getCVV();

   /* private String generateNumberCard() {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder();

        for (int i = 0; i >= 3; i++) {
            cardNumber.append(random.nextInt(10000));
            if (i < 3) {
                cardNumber.append("-");
            }
        }

        return cardNumber.toString();
    }

    private int generateRandomCvv() {
        Random random = new Random();
        return random.nextInt(900) + 100;
    }

    */












}

