package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {
    private Long id;
    private double amount;
    private Integer payments;
    private Long loanId;
    private String name;

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
        this.loanId = clientLoan.getLoan().getId();
        this.name = clientLoan.getLoan().getName();
    }

    public Long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public Long getLoanId() {
        return loanId;
    }

    public String getName() {
        return name;
    }
}
