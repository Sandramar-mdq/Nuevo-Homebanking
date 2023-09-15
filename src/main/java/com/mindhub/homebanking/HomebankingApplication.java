package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	/*@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository,
									  TransactionRepository transactionRepository, LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository, CardRepository cardRepository){
		return args -> {

			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("1111"));
			Client client2 = new Client("Juan", "Perez", "homer@mindhub.com", passwordEncoder.encode("1111"));
			Client client3 = new Client("Maria", "Rios", "marios@minhub.com", passwordEncoder.encode("1111"));

			Client client4 = new Client("admin", "admin", "admin@mindhub.com",passwordEncoder.encode("1111"));


			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(client3);
			clientRepository.save(client4);

			Account account1 = new Account("VIN001", LocalDate.now(), 5000.0);
			Account account2 = new Account( "VIN002", LocalDate.now().plus(Period.ofDays(1)), 7500.0);

			client1.addAccount(account1);
			client1.addAccount(account2);

			accountRepository.save(account1);
			accountRepository.save(account2);


			Transaction transaction1 = new Transaction( 5000.0, "description 1", LocalDateTime.now(), TransactionType.CREDITO );
			Transaction transaction2 = new Transaction( -1000.0, "description 2", LocalDateTime.now().plusDays(2), TransactionType.DEBITO );

			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);


			Loan loan1 = new Loan("Hipotecario", 500000.0, List.of(12,24,36,48,60) );
			Loan loan2 = new Loan( "Personal", 100000.0, List.of(6, 12, 24));
			Loan loan3 = new Loan( "Automotriz", 3000000.0, List.of(6, 12, 24, 36));

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);


			ClientLoan clientLoan1 = new ClientLoan( 400000.0, 60 );
			ClientLoan clientLoan2 = new ClientLoan( 50000.0, 12);

			clientLoan1.setClient(client1);
			clientLoan1.setLoan(loan1);

			clientLoan2.setClient(client1);
			clientLoan2.setLoan(loan2);

			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);

			ClientLoan clientLoan3 = new ClientLoan( 100000.0, 24 );
			ClientLoan clientLoan4 = new ClientLoan( 20000.0 ,36);


			clientLoan3.setClient(client2);
			clientLoan3.setLoan(loan2);

			clientLoan4.setClient(client2);
			clientLoan4.setLoan(loan3);

			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			Card card1 = new Card(client1.getFirstName()+" "+ client1.getLastName(), "1111 2222 3333 4444", LocalDate.now(), LocalDate.now().plusYears(5),CardType.DEBIT, CardColor.GOLD, 131);
			Card card2 = new Card(client1.getFirstName()+" "+ client1.getLastName(), "1222 1333 1444 1555", LocalDate.now(), LocalDate.now().plusYears(5),CardType.CREDIT, CardColor.TITANIUM, 313);

			client1.addCard(card1);
			cardRepository.save(card1);

			client1.addCard(card2);
			cardRepository.save(card2);




		};
	}

	 */
}

