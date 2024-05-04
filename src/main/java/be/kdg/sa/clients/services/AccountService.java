package be.kdg.sa.clients.services;

import be.kdg.sa.clients.controller.dto.AccountDto;
import be.kdg.sa.clients.domain.Account;
import be.kdg.sa.clients.repositories.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account createAccount(AccountDto accountDto){
        // TODO
        // implement error catch
        // check if user already exists

        int points = 0; // To check
        Account account = new Account();
        account.setAccountId(UUID.randomUUID());
        account.setLastName(accountDto.getLastName());
        account.setFirstName(accountDto.getFirstName());
        account.setEmail(accountDto.getEmail());
        account.setCompany(accountDto.getCompany());
        account.setPoints(points);
        account.setType(accountDto.getType());

       return accountRepository.save(account);
    }

    @Transactional
    public void deleteAccount(UUID accountId){
        Account account = accountRepository.findByAccountId(accountId);
        accountRepository.deleteByAccountId(accountId);
    }
    public boolean exists(UUID accountId){
        Account accountExists = accountRepository.findByAccountId(accountId);
        return accountExists != null;
    }

    public Optional<Account> getAccountByLastName(String lastName) {
        return accountRepository.findAccountByLastName(lastName);
    }
}
