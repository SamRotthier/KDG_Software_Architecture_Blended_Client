package be.kdg.sa.clients.controller;

import be.kdg.sa.clients.controller.dto.AccountDto;
import be.kdg.sa.clients.controller.dto.LoyaltyDto;
import be.kdg.sa.clients.domain.Account;
import be.kdg.sa.clients.domain.Enum.OrderStatus;
import be.kdg.sa.clients.domain.Order;
import be.kdg.sa.clients.services.AccountService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
/* mport org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult; */
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    private final AccountService accountService;
    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@Valid @RequestBody AccountDto accountDto){
       accountService.createAccount(accountDto);
       ResponseEntity<String> response = accountService.createKeycloakUser(accountDto);
       return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/auth")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        ResponseEntity<String> response = accountService.authKeycloakUser(username, password);
        return ResponseEntity.ok(response.getBody());
    }

    @PreAuthorize("hasAnyAuthority('user')")
    @DeleteMapping("/{accountId}/delete")
    public ResponseEntity<?> deleteAccount(@PathVariable ("accountId") UUID accountId) {
        if(accountId == null || !accountService.exists(accountId)){
            return ResponseEntity.badRequest().body("Invalid request body");
        }
        accountService.deleteAccount(accountId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Account was successfully deleted");
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/{lastName}")
    public ResponseEntity<?> getAccountByLastName(@PathVariable String lastName){
        Optional<Account> accountOptional = accountService.getAccountByLastName(lastName);
        if(accountOptional.isPresent()){
            return ResponseEntity.ok(accountOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAnyAuthority('user')")
    @GetMapping("/{accountId}/history")
    public ResponseEntity<?> getHistoryByAccountId(
            @PathVariable ("accountId") UUID accountId,
            @RequestParam(value = "Status", required = false) OrderStatus status,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat (iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat (iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime endDate){
        if(accountId == null || !accountService.exists(accountId)){
            return ResponseEntity.badRequest().body("Invalid request body");
        }
        List<Order> history = accountService.getAccountHistory(accountId, status, startDate, endDate);
        return ResponseEntity.status(HttpStatus.OK).body(history);
    }

    @PreAuthorize("hasAnyAuthority('user')")
    @GetMapping("/{accountId}/loyalty")
    public ResponseEntity<?> getLoyaltyByAccountId(
            @PathVariable("accountId") UUID accountId){
        LoyaltyDto loyaltyDto = accountService.getLoyaltyByAccountId(accountId);
        if(loyaltyDto != null){
            return ResponseEntity.status(HttpStatus.OK).body(accountId);
        } else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found for ID: " +accountId);
        }
    }




}
