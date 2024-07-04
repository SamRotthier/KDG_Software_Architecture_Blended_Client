package be.kdg.sa.clients.repositories;

import be.kdg.sa.clients.domain.Account;
import be.kdg.sa.clients.domain.Enum.AccountRelationType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AccountRepositoryTest {
    @Autowired
    AccountRepository accountRepository;

    private Account account;
    private UUID accountId;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setLastName("UnitTestLast");
        account.setFirstName("UnitTestFirst");
        account.setEmail("unittest.test@gmail.com");
        account.setPoints(1000);
        account.setCompany("UnitTest BV");
        account.setType(AccountRelationType.B2B);

    }

    @AfterEach
    @Transactional
    void tearDown() {
        accountRepository.deleteByAccountId(accountId);
        accountRepository.deleteAccountByLastName("UnitTestLast");
    }

    @Test
    public void FindByAccountIdShouldRetrieveAccount() {
        System.out.println("Saving account with ID: " + account.getAccountId());
        Account savedAccount = accountRepository.save(account);
        UUID savedAccountId = savedAccount.getAccountId();
        Account foundAccount = accountRepository.findByAccountId(savedAccountId);
        assertNotNull(foundAccount);
    }

    @Test
    public void DeleteByAccountIdShouldDeleteUniqueAccount() {
        accountRepository.save(account);
        accountRepository.deleteByAccountId(accountId);
        Account foundAccount = accountRepository.findByAccountId(accountId);
        assertNull(foundAccount);
    }

    @Test
    public void FindAccountByLastNameShouldRetrieveAccountForLastName() {
        account.setLastName("RetrieveName");
        accountRepository.save(account);
        Optional<Account> foundAccount = accountRepository.findAccountByLastName("RetrieveName");
        assertTrue(foundAccount.isPresent());
        assertEquals("RetrieveName", foundAccount.get().getLastName());
    }

    @Test
    public void ExistsByEmailShouldReturnBoolean() {
        accountRepository.save(account);
        boolean exists = accountRepository.existsByEmail("unittest.test@gmail.com");
        assertTrue(exists);

        boolean doesNotExist = accountRepository.existsByEmail("nonexistent@gmail.com");
        assertFalse(doesNotExist);
    }

}