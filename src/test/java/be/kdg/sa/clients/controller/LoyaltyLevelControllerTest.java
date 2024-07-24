package be.kdg.sa.clients.controller;

import be.kdg.sa.clients.controller.dto.LoyaltyLevelDto;
import be.kdg.sa.clients.domain.LoyaltyLevel;
import be.kdg.sa.clients.repositories.LoyaltyLevelRepository;
import be.kdg.sa.clients.services.LoyaltyLevelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class LoyaltyLevelControllerTest {
    @Autowired
    private LoyaltyLevelService loyaltyLevelService;
    @Autowired
    private LoyaltyLevelRepository loyaltyLevelRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private LoyaltyLevel bronze;
    private LoyaltyLevel silver;
    private LoyaltyLevel gold;
    private LoyaltyLevel platinum;

    private LoyaltyLevel savedBronze;
    private LoyaltyLevel savedSilver;
    private LoyaltyLevel savedGold;
    private LoyaltyLevel savedPlatinum;

    @BeforeEach
    void setUp() {
        bronze = new LoyaltyLevel("BRONZE", 0.00, 0, 999);
        savedBronze = loyaltyLevelRepository.saveAndFlush(bronze);
        silver = new LoyaltyLevel("SILVER", 0.05, 1000, 4999);
        savedSilver = loyaltyLevelRepository.saveAndFlush(silver);
        gold = new LoyaltyLevel("GOLD", 0.10, 5000, 10000);
        savedGold = loyaltyLevelRepository.saveAndFlush(gold);
        platinum = new LoyaltyLevel("PLATINUM", 0.20, 100001, 2147483646);
        savedPlatinum = loyaltyLevelRepository.saveAndFlush(platinum);
    }

    @AfterEach
    void tearDown() {
        loyaltyLevelRepository.deleteById(savedBronze.getId());
        loyaltyLevelRepository.deleteById(savedSilver.getId());
        loyaltyLevelRepository.deleteById(savedGold.getId());
        loyaltyLevelRepository.deleteById(savedPlatinum.getId());
    }

    @Test
    @WithMockUser(authorities = "admin")
    void getAllLevelsShouldReturnStatusOk() throws Exception {
        mockMvc.perform(get("/loyaltylevels"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("BRONZE"))
                .andExpect(jsonPath("$[1].name").value("SILVER"))
                .andExpect(jsonPath("$[2].name").value("GOLD"))
                .andExpect(jsonPath("$[3].name").value("PLATINUM"));
    }

    @Test
    @WithMockUser(authorities = "admin")
    void getLevelByIdShouldReturnStatusOk() throws Exception{
        mockMvc.perform(get("/loyaltylevels/{id}", savedBronze.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("BRONZE"))
                .andExpect(jsonPath("$.discount").value(0.00))
                .andExpect(jsonPath("$.minPoints").value(0))
                .andExpect(jsonPath("$.maxPoints").value(999));
    }
    @Test
    @WithMockUser(authorities = "admin")
    void getLevelByIdShouldReturnStatusNotFound() throws Exception {
        mockMvc.perform(get("/loyaltylevels/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "admin")
    void createLoyaltyLevelShouldReturnStatusCreated() throws Exception {
        LoyaltyLevelDto loyaltyLevelDto = new LoyaltyLevelDto("Diamond", 0.25, 20000, 50000);

        mockMvc.perform(post("/loyaltylevels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loyaltyLevelDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Diamond"))
                .andExpect(jsonPath("$.discount").value(0.25))
                .andExpect(jsonPath("$.minPoints").value(20000))
                .andExpect(jsonPath("$.maxPoints").value(50000));
    }

    @Test
    @WithMockUser(authorities = "admin")
    void updateLoyaltyLevelShouldReturnStatusOk() throws Exception {
        LoyaltyLevelDto loyaltyLevelDto = new LoyaltyLevelDto("Bronze Updated", 0.02, 0, 999);

        mockMvc.perform(put("/loyaltylevels/{id}", savedBronze.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loyaltyLevelDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bronze Updated"))
                .andExpect(jsonPath("$.discount").value(0.02))
                .andExpect(jsonPath("$.minPoints").value(0))
                .andExpect(jsonPath("$.maxPoints").value(999));
    }

    @Test
    @WithMockUser(authorities = "admin")
    void updateLoyaltyLevelShouldReturnStatusNotFound() throws Exception {
        LoyaltyLevelDto loyaltyLevelDto = new LoyaltyLevelDto("Does not exist", 0.50, 0, 999);

        mockMvc.perform(put("/loyaltylevels/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loyaltyLevelDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "admin")
    void getDiscountShouldReturnStatusOk() throws Exception {
        mockMvc.perform(get("/loyaltylevels/discount")
                        .param("points", "6000"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(0.10));
    }

    @Test
    @WithMockUser(authorities = "admin")
    void getLevelShouldReturnStatusOk() throws Exception {
        mockMvc.perform(get("/loyaltylevels/level")
                        .param("points", "6000"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("GOLD"));
    }
}