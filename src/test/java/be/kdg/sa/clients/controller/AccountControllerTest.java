package be.kdg.sa.clients.controller;

import be.kdg.sa.clients.controller.dto.AccountDto;
import be.kdg.sa.clients.domain.Enum.AccountRelationType;
import be.kdg.sa.clients.repositories.AccountRepository;
import be.kdg.sa.clients.services.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AccountControllerTest {
    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ObjectMapper objectMapper;
    private AccountDto accountDto;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        accountDto = new AccountDto("IntegrationTestLast", "IntegrationTestFirst", "integrationtest.test@gmail.com", "integrationTestUser", "password", "Integration Test BV", AccountRelationType.B2B);
    }

    @AfterEach
    void tearDown() {
        accountRepository.deleteAccountByLastName("IntegrationTestLast");
    }

    @Test
    void createAccount() throws Exception {
        mockMvc.perform(post("/accounts/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto)))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) content().string("Account created successfully"));
    }

    @Test
    void login() throws Exception{
        accountService.createAccount(accountDto); // Assuming this also creates the user in Keycloak for testing purposes
        mockMvc.perform(post("/accounts/auth")
                        .param("username", accountDto.getUsername())
                        .param("password", accountDto.getPassword()))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) content().string("Bearer token..."));
    }

    @Test
    void deleteAccount() {
    }

    @Test
    void getAccountByLastName() {
    }

    @Test
    void getHistoryByAccountId() {
    }

    @Test
    void getLoyaltyByAccountId() {
    }
}