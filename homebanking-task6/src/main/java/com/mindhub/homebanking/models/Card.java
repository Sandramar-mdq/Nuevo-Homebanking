package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    private Long id;
    private String cardHolder;
    private String number;
    private LocalDate fromDate;
    private LocalDate thruDate;
    private CardType type;
    private CardColor color;
    private Integer cvv;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "card_n")
    private Client cards;

    public Card() {
    }

    public Card(String cardHolder, String number, LocalDate fromDate,
                LocalDate thruDate, CardType type, CardColor color, Integer cvv) {
        this.cardHolder = cardHolder;
        this.number = number;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.type = type;
        this.color = color;
        this.cvv = cvv;
    }


    public Long getId() {
        return id;
    }

    public String getCardHolder() {
        return cardHolder;
    }
    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }
    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }
    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }

    public CardType getType() {
        return type;
    }
    public void setType(CardType type) {
        this.type = type;
    }

    public CardColor getColor() {
        return color;
    }
    public void setColor(CardColor color) {
        this.color = color;
    }

    public Integer getCvv() {
        return cvv;
    }
    public void setCvv(Integer cvv) {
        this.cvv = cvv;
    }

    public void setClient(Client cards) {
        this.cards = cards;
    }


    public Client getCard() {
        return cards;
    }
}
