package be.kdg.sa.clients.services;

import be.kdg.sa.clients.controller.dto.AccountDto;
import be.kdg.sa.clients.controller.dto.LoyaltyDto;
import be.kdg.sa.clients.domain.Account;
import be.kdg.sa.clients.domain.Enum.LoyaltyLevel;
import be.kdg.sa.clients.domain.Enum.OrderStatus;
import be.kdg.sa.clients.domain.Order;
import be.kdg.sa.clients.repositories.AccountRepository;
import be.kdg.sa.clients.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
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

    public List<Order> getAccountHistory(UUID accountId, OrderStatus orderStatus, LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Fetching all orders for account: {}", accountId);
        List<Order> orders = orderRepository.findAllByAccountId(accountId);

        if(orderStatus != null){
            logger.info("Filtering orders based on order status: {}", orderStatus);
            orders = orders.stream().filter(order -> order.getStatus().equals(orderStatus)).toList();
        }

        if(startDate != null && endDate != null){
            logger.info("Filtering orders based on date range from {} until {}", startDate, endDate);
            orders = orders.stream().filter(order -> !order.getCreationDateTime().isBefore(startDate) && !order.getCreationDateTime().isAfter(endDate)).toList();
        }

        return orders;
    }

    public LoyaltyDto getLoyaltyByAccountId(UUID accountId) {
        logger.info("Retrieving account info for ID: {}", accountId);
        Account account = accountRepository.findByAccountId(accountId);

        if(account != null){
            logger.info("account found for ID: {}", accountId);
            LoyaltyDto loyaltyDto = new LoyaltyDto();
            loyaltyDto.setAccountId(accountId);
            loyaltyDto.setFirstName(account.getFirstName());
            loyaltyDto.setLastName(account.getLastName());
            loyaltyDto.setLoyaltyLevel(account.getLoyaltyLevel());
            loyaltyDto.setPoints(account.getPoints());
            return loyaltyDto;
        } else{
            logger.warn("No account found for ID: {}", accountId);
            return null;
        }
    }

    public void updateLoyaltyPointsAndLevel(UUID accountId, int orderPoints){
        Account account = accountRepository.findByAccountId(accountId);

        if(account != null) {
            //Calculate and update Loyalty Points
            logger.info("Calculating and updating the loyalty points of the user: {}", accountId);
            int points = account.getPoints() + orderPoints;
            account.setPoints(points);

            //Check and update Loyalty Level
            logger.info("Checking and updating loyalty level of user: {}", accountId);
            LoyaltyLevel loyaltyLevel = LoyaltyLevel.getLoyaltyLevel(points);
            if (loyaltyLevel != account.getLoyaltyLevel()) {
                account.setLoyaltyLevel(loyaltyLevel);
            }

            accountRepository.save(account);
            logger.info("Account updated for Id: {}", accountId);
        } else{
            logger.warn("Account with ID: {} was not found", accountId);
        }

    }
}
