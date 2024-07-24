package be.kdg.sa.clients.repositories;

import be.kdg.sa.clients.domain.LoyaltyLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoyaltyLevelRepository extends JpaRepository<LoyaltyLevel, Long> {
    List<LoyaltyLevel> findAll();

}
