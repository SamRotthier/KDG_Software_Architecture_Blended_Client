package be.kdg.sa.clients.services;

import be.kdg.sa.clients.controller.dto.AccountDto;
import be.kdg.sa.clients.controller.dto.KeycloakDto;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);
    private final AccountRepository accountRepository;
    private final OrderRepository orderRepository;

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    public AccountService(AccountRepository accountRepository, OrderRepository orderRepository) {
        this.accountRepository = accountRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Account createAccount(AccountDto accountDto){
        logger.info("Creating account for email: {}", accountDto.getEmail());

        if (accountRepository.existsByEmail(accountDto.getEmail())) {
            logger.warn("Account creation failed: Account with email {} already exists.", accountDto.getEmail());
            return null;
        } else {

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

    public ResponseEntity<String> createKeycloakUser(AccountDto accountDto) {
        logger.info("Creating user in Keycloak");
        String url = authServerUrl + "/admin/realms/" + realm + "/users";

        Account account = accountRepository.findAccountByEmail(accountDto.getEmail());

        KeycloakDto keycloakDto = new KeycloakDto(accountDto.getLastName(), accountDto.getFirstName(), accountDto.getUsername(), accountDto.getPassword(), accountDto.getEmail(), account.getAccountId().toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(getAdminAccessToken());

        HttpEntity<KeycloakDto> request = new HttpEntity<>(keycloakDto, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            logger.info("User created successfully");
        } else {
            logger.error("Failed to create user: " + response.getStatusCode());
        }
        return response;
    }

    public String getAdminAccessToken() {
        logger.info("Retrieve admin access token");

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("grant_type", "client_credentials");

        ResponseEntity<Map> response = sendTokenRequest(requestBody);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            logger.info("Admin access token retrieved successfully.");
            return (String) response.getBody().get("access_token");
        } else {
            logger.error("Failed to obtain access token");
            return null;
        }
    }

    public ResponseEntity<String> authKeycloakUser(String username, String password){
        logger.info("Fetching auth token");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("username", username);
        params.add("password", password);
        params.add("grant_type", "password");

        ResponseEntity<Map> response = sendTokenRequest(params);
        ResponseEntity<String> responseString = new ResponseEntity<>(response.getBody().toString(), response.getStatusCode());

        if (response.getStatusCode().is2xxSuccessful()) {
            logger.info("User created successfully");
        } else {
            logger.error("Failed to create user: " + response.getStatusCode());
        }
        return responseString;
    }

    private ResponseEntity<Map> sendTokenRequest(MultiValueMap<String, String> params){
        String tokenUrl = authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(tokenUrl, request, Map.class);

    }

}
