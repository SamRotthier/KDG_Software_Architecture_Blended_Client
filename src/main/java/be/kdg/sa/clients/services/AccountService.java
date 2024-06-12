package be.kdg.sa.clients.services;

import be.kdg.sa.clients.controller.dto.AccountDto;
import be.kdg.sa.clients.domain.Account;
import be.kdg.sa.clients.domain.Order;
import be.kdg.sa.clients.repositories.AccountRepository;
import be.kdg.sa.clients.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);
    private final AccountRepository accountRepository;
    private final OrderRepository orderRepository;

    public AccountService(AccountRepository accountRepository, OrderRepository orderRepository) {
        this.accountRepository = accountRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Account createAccount(AccountDto accountDto){
        logger.info("Creating account for email: {}", accountDto.getEmail());
        // TODO
        // check if user already exists

        if (accountRepository.existsByEmail(accountDto.getEmail())) {
            logger.warn("Account creation failed: Account with email {} already exists.", accountDto.getEmail());
        }

        int points = 0; // To check
        Account account = new Account();
        account.setAccountId(UUID.randomUUID());
        account.setLastName(accountDto.getLastName());
        account.setFirstName(accountDto.getFirstName());
        account.setEmail(accountDto.getEmail());
        account.setCompany(accountDto.getCompany());
        account.setPoints(points);
        account.setType(accountDto.getType());

        Account savedAccount = accountRepository.save(account);
        logger.info("Account created successfully: {}", savedAccount);

       return savedAccount;
    }

    @Transactional
    public void deleteAccount(UUID accountId){
        logger.info("Deleting account with ID: {}", accountId);
        Account account = accountRepository.findByAccountId(accountId);
        if(account != null){
            orderRepository.nullifyAccountId(account.getAccountId());
            logger.info("Account ID for related orders was successfully nullified.");
            accountRepository.deleteByAccountId(account.getAccountId());
            logger.info("Account with ID {} deleted successfully", accountId);
        } else {
            logger.warn("Delete failed: Account with ID {} not found", accountId);
        }
    }
    public boolean exists(UUID accountId){
        logger.info("Checking existence of account with ID: {}", accountId);
        Account accountExists = accountRepository.findByAccountId(accountId);
        return accountExists != null;
    }

    public Optional<Account> getAccountByLastName(String lastName) {
        logger.info("Fetching account with last name: {}", lastName);
        return accountRepository.findAccountByLastName(lastName);
    }

    public List<Order> getAccountHistory(UUID accountId) {
        return orderRepository.findAllByAccountId(accountId);
    }
}
