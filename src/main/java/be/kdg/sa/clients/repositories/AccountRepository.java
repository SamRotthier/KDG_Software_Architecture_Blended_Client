package be.kdg.sa.clients.repositories;

import be.kdg.sa.clients.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    Account findByAccountId(UUID accountId);
    Integer deleteByAccountId(UUID accountId);

    Optional<Account> findAccountByLastName(String lastName);

    boolean existsByEmail(String email);
}
