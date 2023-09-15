package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;

public class LoanApplicationDTO {
        private Long loanId;
        public static Double amount;
        private Double maxAmount;
        private static Integer payments;
        private static String toAccountNumber;

        public LoanApplicationDTO(Loan loan) {
                this.loanId = getLoanId();
                this.amount = getAmount();
                this.maxAmount = getMaxAmount();
                this.payments = getPayments();
                this.toAccountNumber = toAccountNumber();
        }

        public Long getLoanId() {
                return loanId;
        }

        public  Double getAmount() {
                return amount;
        }

        public  Integer getPayments() {
                return payments;
        }

        public Double getMaxAmount() {
                return maxAmount;
        }

        public  String toAccountNumber() {
                return toAccountNumber;
        }

        public void setAmount(double amountToAccredit) {
        }

        public void getDescription(String s) {

        }
}
