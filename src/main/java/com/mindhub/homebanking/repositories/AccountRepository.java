package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository <Account, Long> {
    public List<Account> findByClient(Client client);

    Account findByNumber(String accountNumber);

}