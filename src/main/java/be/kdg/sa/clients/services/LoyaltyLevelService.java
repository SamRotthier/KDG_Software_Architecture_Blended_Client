package be.kdg.sa.clients.services;

import be.kdg.sa.clients.repositories.LoyaltyLevelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import be.kdg.sa.clients.domain.LoyaltyLevel;

import java.util.List;

@Service
public class LoyaltyLevelService {

    private final LoyaltyLevelRepository loyaltyLevelRepository;
    private static final Logger logger = LoggerFactory.getLogger(LoyaltyLevelService.class);

    public LoyaltyLevelService(LoyaltyLevelRepository loyaltyLevelRepository) {
        this.loyaltyLevelRepository = loyaltyLevelRepository;
    }

    public List<LoyaltyLevel> getAllLevels() {
        logger.info("Retrieve all levels");
        return loyaltyLevelRepository.findAll();
    }

    public double getDiscount(int loyaltyPoints) {
        logger.info("Retrieve discount");
         double discount = loyaltyLevelRepository.findAll().stream()
                .filter(level -> loyaltyPoints >= level.getMinPoints() && loyaltyPoints <= level.getMaxPoints())
                .map(LoyaltyLevel::getDiscount)
                .findFirst()
                .orElse(0.0);
         logger.info(String.valueOf(discount));
        return discount;
    }
    public LoyaltyLevel getLoyaltyLevel(int loyaltyPoints) {
        logger.info("Retrieve level based on points");
        return loyaltyLevelRepository.findAll().stream()
                .filter(level -> loyaltyPoints >= level.getMinPoints() && loyaltyPoints <= level.getMaxPoints())
                .findFirst()
                .orElse(null);
    }

    public LoyaltyLevel addLevel(LoyaltyLevel level) {
        logger.info("Save new level");
        LoyaltyLevel savedLevel = loyaltyLevelRepository.save(level);
        return savedLevel;
    }

    public LoyaltyLevel updateLevel(LoyaltyLevel level) {
        logger.info("Update level", level.getName());
        LoyaltyLevel savedLevel = loyaltyLevelRepository.save(level);
        return savedLevel;
    }

    public LoyaltyLevel getLevelById(Long id) {
        return loyaltyLevelRepository.findById(id).orElse(null);
    }
}
