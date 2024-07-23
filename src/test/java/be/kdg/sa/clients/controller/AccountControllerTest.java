package be.kdg.sa.clients.controller;

import be.kdg.sa.clients.controller.dto.AccountDto;
import be.kdg.sa.clients.domain.Enum.AccountRelationType;
import be.kdg.sa.clients.domain.Enum.OrderStatus;
import be.kdg.sa.clients.repositories.AccountRepository;
import be.kdg.sa.clients.services.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private OrderStatus orderStatus;

    @Autowired
    private MockMvc mockMvc;

    private static final Logger logger = LoggerFactory.getLogger(AccountControllerTest.class);

    @BeforeEach
    void setUp() {
        accountDto = new AccountDto("IntegrationTestLast", "IntegrationTestFirst", "integrationtest2.test@gmail.com", "integrationTestUser2", "password", "Integration Test BV", AccountRelationType.B2B);
    }

    @AfterEach
    void tearDown() {
        accountRepository.deleteAccountByLastName("IntegrationTestLast");
    }

    @Test
    void createAccountShouldCreateAccountInDbAndKeyCloak() throws Exception {
        mockMvc.perform(post("/accounts/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto)))
                .andExpect(status().isOk())
                .andDo(result -> logger.info(result.getResponse().getContentAsString()));
    }

    @Test
    void loginShouldReturnAccessToken() throws Exception{
        mockMvc.perform(post("/accounts/auth")
                        .param("username", "testId2")
                        .param("password", "testid2"))
                .andExpect(status().isOk())
                .andDo(result -> logger.info(result.getResponse().getContentAsString()));
    }

    @Test
    @WithMockUser(authorities = "user")
    void deleteAccountShouldReturnStatusNoContent() throws Exception {
        UUID accountId = accountService.createAccount(accountDto).getAccountId();
        mockMvc.perform(delete("/accounts/" + accountId + "/delete"))
                .andExpect(status().isNoContent())
                .andDo(result -> logger.info(result.getResponse().getContentAsString()));
    }

    @Test
    @WithMockUser(authorities = "admin")
    void getAccountByLastNameShouldReturnCorrectAccount() throws Exception {
        accountService.createAccount(accountDto);
        mockMvc.perform(get("/accounts/" + accountDto.getLastName()))
                .andExpect(status().isOk())
                .andDo(result -> logger.info(result.getResponse().getContentAsString()));
    }

    @Test
    @WithMockUser(authorities = "user")
    void getHistoryByAccountIdForOrderStatusConfirmedStartDateAndEndDateShouldReturnOk() throws Exception {
        UUID accountId = accountService.createAccount(accountDto).getAccountId();
        mockMvc.perform(get("/accounts/" + accountId + "/history")
                        .param("Status", OrderStatus.CONFIRMED.toString())
                        .param("startDate", LocalDateTime.now().minusDays(100).toString())
                        .param("endDate", LocalDateTime.now().toString()))
                .andExpect(status().isOk())
                .andDo(result -> logger.info(result.getResponse().getContentAsString()));
    }

    @Test
    @WithMockUser(authorities = "user")
    void getLoyaltyByAccountIdShouldReturnLoyaltyPoints() throws Exception{
        UUID accountId = accountService.createAccount(accountDto).getAccountId();
        mockMvc.perform(get("/accounts/" + accountId + "/loyalty"))
                .andExpect(status().isOk())
                .andDo(result -> logger.info(result.getResponse().getContentAsString()));
    }
}