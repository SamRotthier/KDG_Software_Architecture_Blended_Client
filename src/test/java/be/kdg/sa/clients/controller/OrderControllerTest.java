package be.kdg.sa.clients.controller;

import be.kdg.sa.clients.controller.dto.OrderDto;
import be.kdg.sa.clients.controller.dto.OrderProductDto;
import be.kdg.sa.clients.domain.Account;
import be.kdg.sa.clients.domain.Enum.AccountRelationType;
import be.kdg.sa.clients.domain.Enum.LoyaltyLevel;
import be.kdg.sa.clients.domain.Enum.OrderStatus;
import be.kdg.sa.clients.domain.Enum.ProductState;
import be.kdg.sa.clients.domain.Order;
import be.kdg.sa.clients.domain.OrderProduct;
import be.kdg.sa.clients.domain.Product;
import be.kdg.sa.clients.repositories.AccountRepository;
import be.kdg.sa.clients.repositories.OrderProductRepository;
import be.kdg.sa.clients.repositories.OrderRepository;
import be.kdg.sa.clients.repositories.ProductRepository;
import be.kdg.sa.clients.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    private UUID order_id ;
    private UUID product_id_1 ;
    private UUID product_id_2 ;
    private Order orderTest;
    private OrderDto orderDtoTest;
    @BeforeEach
    void setUp() {
        order_id = UUID.randomUUID();
        product_id_1 = UUID.randomUUID();
        product_id_2 = UUID.randomUUID();

        orderTest = new Order();
        orderTest.setOrderId(order_id);
        orderTest.setStatus(OrderStatus.PENDING);
        orderRepository.saveAndFlush(orderTest);

        Account account = new Account();
        account.setAccountId(UUID.randomUUID());
        account.setFirstName("TestFirst");
        account.setLastName("TestLast");
        account.setType(AccountRelationType.B2B);
        account.setPoints(100);
        account.setEmail("test@test.com");
        account.setCompany("TestCompany");
        accountRepository.saveAndFlush(account);

        Product product1 = new Product();
        product1.setName("Product1");
        product1.setProductId(product_id_1);
        product1.setPrice(BigDecimal.TEN);
        product1.setProductState(ProductState.ACTIVE);
        productRepository.saveAndFlush(product1);

        Product product2 = new Product();
        product1.setName("Product2");
        product1.setProductId(product_id_2);
        product1.setPrice(BigDecimal.TEN);
        product1.setProductState(ProductState.ACTIVE);
        productRepository.saveAndFlush(product2);

        OrderProductDto productDto1 = new OrderProductDto();
        productDto1.setProductId(product1.getProductId());
        productDto1.setQuantity(5);
        OrderProductDto productDto2 = new OrderProductDto();
        productDto2.setProductId(product2.getProductId());
        productDto2.setQuantity(7);


        List<OrderProductDto> productDtoList = new ArrayList<>();
        productDtoList.add(productDto1);
        productDtoList.add(productDto2);

        orderDtoTest = new OrderDto();
        orderDtoTest.setAccountId(account.getAccountId());
        orderDtoTest.setProducts(productDtoList);
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteById(order_id);
    }

    @WithMockUser(authorities = "admin")
    @Test
    void retrievingOrdersShouldReturnStatus200() throws Exception {
        mockMvc.perform(get("/orders/"))
                .andExpect(status().isOk());
    }

    @WithMockUser(authorities = "user")
    @Test
    void createOrderShouldReturnStatus201() throws Exception {
        mockMvc.perform(post("/orders/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDtoTest)))
                .andExpect(status().isCreated())
                .andExpect((ResultMatcher) content().string("The order was successfully created"));
    }

    @Test
    void createCopyOrder() {
    }

    @Test
    void confirmOrder() {
    }

    @Test
    void cancelOrder() {
    }

    @Test
    void getOrder() {
    }
}