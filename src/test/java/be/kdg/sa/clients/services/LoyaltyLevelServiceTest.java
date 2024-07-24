package be.kdg.sa.clients.services;

import be.kdg.sa.clients.domain.LoyaltyLevel;
import be.kdg.sa.clients.repositories.LoyaltyLevelRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class LoyaltyLevelServiceTest {

    @MockBean
    private LoyaltyLevelRepository loyaltyLevelRepository;

    @Autowired
    private LoyaltyLevelService loyaltyLevelService;
    private LoyaltyLevel bronze;
    private LoyaltyLevel silver;


    @BeforeEach
    void setUp() {
        bronze = new LoyaltyLevel("Bronze", 0.00, 0, 999);
        silver = new LoyaltyLevel("Silver", 0.05, 1000, 4999);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllLevelsShouldReturnAllLoyaltyLevels() {
        when(loyaltyLevelRepository.findAll()).thenReturn(Arrays.asList(bronze, silver));

        List<LoyaltyLevel> levels = loyaltyLevelService.getAllLevels();

        assertEquals(2, levels.size());
        verify(loyaltyLevelRepository, times(1)).findAll();
    }

    @Test
    void getDiscountShouldReturnDiscountPerc() {
        when(loyaltyLevelRepository.findAll()).thenReturn(Arrays.asList(bronze, silver));

        double discount = loyaltyLevelService.getDiscount(1100);

        assertEquals(0.05, discount);
        verify(loyaltyLevelRepository, times(1)).findAll();
    }

    @Test
    void getLoyaltyLevelShouldReturnLoyaltyLevel() {
        when(loyaltyLevelRepository.findAll()).thenReturn(Arrays.asList(bronze, silver));

        LoyaltyLevel level = loyaltyLevelService.getLoyaltyLevel(1100);

        assertNotNull(level);
        assertEquals("Silver", level.getName());
        verify(loyaltyLevelRepository, times(1)).findAll();
    }

    @Test
    void addLevelShouldSaveSuccessfully() {
        LoyaltyLevel newLevel = new LoyaltyLevel("Gold", 0.10, 5000, 10000);
        LoyaltyLevel savedLevel = new LoyaltyLevel("Gold", 0.10, 5000, 10000);
        when(loyaltyLevelRepository.save(newLevel)).thenReturn(savedLevel);

        // Act
        LoyaltyLevel result = loyaltyLevelService.addLevel(newLevel);

        // Assert
        assertNotNull(result);
        assertEquals("Gold", result.getName());
        verify(loyaltyLevelRepository, times(1)).save(newLevel);
    }

    @Test
    void updateLevelShouldSaveSuccessfully() {
        when(loyaltyLevelRepository.findById(1L)).thenReturn(Optional.of(bronze));

        // Act
        LoyaltyLevel result = loyaltyLevelService.getLevelById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Bronze", result.getName());
        verify(loyaltyLevelRepository, times(1)).findById(1L);
    }

}