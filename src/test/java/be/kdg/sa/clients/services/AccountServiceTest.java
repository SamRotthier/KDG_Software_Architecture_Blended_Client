package be.kdg.sa.clients.services;

import be.kdg.sa.clients.controller.dto.AccountDto;
import be.kdg.sa.clients.controller.dto.LoyaltyDto;
import be.kdg.sa.clients.domain.Account;
import be.kdg.sa.clients.domain.Enum.AccountRelationType;
import be.kdg.sa.clients.domain.Enum.LoyaltyLevel;
import be.kdg.sa.clients.domain.Order;
import be.kdg.sa.clients.repositories.AccountRepository;
import be.kdg.sa.clients.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@SpringBootTest
class AccountServiceTest {

    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private RestTemplate restTemplate;
    @Autowired
    private AccountService accountService;

    private AccountDto accountDto;

    @BeforeEach
    void setUp() {
        accountDto = new AccountDto("TestLast", "TestFirst", "test.test@gmail.com", "testuser", "password", "Test BV", AccountRelationType.B2B);
    }

    @AfterEach
    void tearDown() {
        reset(accountRepository, orderRepository, restTemplate);

    }

    @Test
    public void CreateAccountShouldCreateNewAccount() {
        given(accountRepository.existsByEmail(accountDto.getEmail())).willReturn(false);
        given(accountRepository.save(any(Account.class))).willAnswer(invocation -> invocation.getArgument(0));

        Account createdAccount = accountService.createAccount(accountDto);

        assertNotNull(createdAccount);
        assertEquals(accountDto.getEmail(), createdAccount.getEmail());
        assertTrue(!accountRepository.existsByEmail(accountDto.getEmail()));
    }

    @Test
    public void NoAccountShouldBeCreatedWhenAlreadyExists() {
        given(accountRepository.existsByEmail(accountDto.getEmail())).willReturn(true);

        Account accountExists = accountService.createAccount(accountDto);
        assertNull(accountExists);
        assertTrue(accountRepository.existsByEmail(accountDto.getEmail()));
    }

    @Test
    public void DeleteAccountShouldDeleteAnExistingAccount() {
        given(accountRepository.existsByEmail(accountDto.getEmail())).willReturn(false);
        given(accountRepository.save(any(Account.class))).willAnswer(invocation -> invocation.getArgument(0));
        Account accountCreated = accountService.createAccount(accountDto);

        UUID accountId = accountCreated.getAccountId();
        given(accountRepository.findByAccountId(accountId)).willReturn(accountCreated);

        given(orderRepository.findAllByAccountId(accountId)).willReturn(Collections.emptyList());
        willDoNothing().given(accountRepository).deleteByAccountId(accountId);
        given(accountRepository.findByAccountId(accountId)).willReturn(null);
        accountService.deleteAccount(accountCreated.getAccountId());

        assertTrue(orderRepository.findAllByAccountId(accountCreated.getAccountId()).isEmpty());
        assertNull(accountRepository.findByAccountId(accountCreated.getAccountId()));
    }

    @Test
    public void NoAccountShouldBeDeletedWhenNotFound() {
        UUID accountId = UUID.randomUUID();
        when(accountRepository.findByAccountId(accountId)).thenReturn(null);

        accountService.deleteAccount(accountId);

        verify(orderRepository, never()).nullifyAccountId(any(UUID.class));
        verify(accountRepository, never()).deleteByAccountId(any(UUID.class));
    }

    @Test
    public void ExistsShouldReturnTrueWhenAccountExists() {
        UUID accountId = UUID.randomUUID();
        when(accountRepository.findByAccountId(accountId)).thenReturn(new Account());

        boolean exists = accountService.exists(accountId);

        assertTrue(exists);
        verify(accountRepository, times(1)).findByAccountId(accountId);
    }

    @Test
    public void ExistsShouldReturnFalseWhenAccountExist() {
        UUID accountId = UUID.randomUUID();
        when(accountRepository.findByAccountId(accountId)).thenReturn(null);

        boolean exists = accountService.exists(accountId);

        assertFalse(exists);
        verify(accountRepository, times(1)).findByAccountId(accountId);
    }

    @Test
    public void ShouldFetchAccountByLastName() {
        String lastName = "TestLast";
        Account account = new Account();
        account.setLastName(lastName);
        when(accountRepository.findAccountByLastName(lastName)).thenReturn(Optional.of(account));

        Optional<Account> result = accountService.getAccountByLastName(lastName);

        assertTrue(result.isPresent());
        assertEquals(lastName, result.get().getLastName());
        verify(accountRepository, times(1)).findAccountByLastName(lastName);
    }

    @Test
        public void ShouldFetchAccountHistory() {
        UUID accountId = UUID.randomUUID();
        List<Order> orders = new ArrayList<>();
        orders.add(new Order());
        when(orderRepository.findAllByAccountId(accountId)).thenReturn(orders);

        List<Order> result = accountService.getAccountHistory(accountId, null, null, null);

        assertEquals(orders, result);
        verify(orderRepository, times(1)).findAllByAccountId(accountId);
    }

    @Test
    public void ShouldFetchLoyaltyByAccountId() {
        UUID accountId = UUID.randomUUID();
        Account account = new Account();
        account.setAccountId(accountId);
        account.setFirstName("TestFirst");
        account.setLastName("TestLast");
        account.setPoints(4500);
        account.setLoyaltyLevel(LoyaltyLevel.SILVER);
        when(accountRepository.findByAccountId(accountId)).thenReturn(account);

        LoyaltyDto loyaltyDto = accountService.getLoyaltyByAccountId(accountId);

        assertNotNull(loyaltyDto);
        assertEquals(accountId, loyaltyDto.getAccountId());
        assertEquals("TestFirst", loyaltyDto.getFirstName());
        assertEquals("TestLast", loyaltyDto.getLastName());
        assertEquals(4500, loyaltyDto.getPoints());
        assertEquals(LoyaltyLevel.SILVER, loyaltyDto.getLoyaltyLevel());
    }

    @Test
    public void testUpdateLoyaltyPointsAndLevel() {
        UUID accountId = UUID.randomUUID();
        Account account = new Account();
        account.setAccountId(accountId);
        account.setPoints(4500);
        account.setLoyaltyLevel(LoyaltyLevel.SILVER);
        when(accountRepository.findByAccountId(accountId)).thenReturn(account);

        accountService.updateLoyaltyPointsAndLevel(accountId, 3000);

        assertEquals(7500, account.getPoints());
        assertEquals(LoyaltyLevel.GOLD, account.getLoyaltyLevel());
    }

    @Test
    public void testAuthKeycloakUser() {
        ResponseEntity<String> response = accountService.authKeycloakUser("test1", "password");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("access_token"));
    }
}