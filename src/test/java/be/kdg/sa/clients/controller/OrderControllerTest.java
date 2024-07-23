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
import be.kdg.sa.clients.services.OrderService;
import be.kdg.sa.clients.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
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
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
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
    private OrderService orderService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    private UUID order_id ;
    private UUID product_id_1 ;
    private UUID product_id_2 ;
    private Order orderTest;
    private OrderDto orderDtoTest;
    private UUID account_id;
    private static final Logger logger = LoggerFactory.getLogger(OrderControllerTest.class);

    @BeforeEach
    void setUp() {
        product_id_1 = UUID.randomUUID();
        product_id_2 = UUID.randomUUID();

        Account account = new Account();
        account.setFirstName("TestFirst");
        account.setLastName("TestLast");
        account.setType(AccountRelationType.B2B);
        account.setPoints(100);
        account.setEmail("test@test.com");
        account.setCompany("TestCompany");
        accountRepository.saveAndFlush(account);

        Account savedAccount = accountRepository.findById(account.getAccountId()).orElseThrow();
        assertNotNull(savedAccount);
        account_id = savedAccount.getAccountId();

        Product product1 = new Product();
        product1.setName("Product1");
        product1.setProductId(product_id_1);
        product1.setPrice(BigDecimal.TEN);
        product1.setProductState(ProductState.ACTIVE);
        productRepository.saveAndFlush(product1);

        Product product2 = new Product();
        product2.setName("Product2");
        product2.setProductId(product_id_2);
        product2.setPrice(BigDecimal.TEN);
        product2.setProductState(ProductState.ACTIVE);
        productRepository.saveAndFlush(product2);

        orderTest = new Order();
        orderTest.setStatus(OrderStatus.PENDING);
        orderTest.setAccount(savedAccount);
        orderTest.setCreationDateTime(LocalDateTime.now());
        orderTest.setTotalPrice(BigDecimal.ZERO);
        orderRepository.saveAndFlush(orderTest);

        Order savedOrder = orderRepository.findById(orderTest.getOrderId()).orElseThrow();
        assertNotNull(savedOrder);
        order_id = savedOrder.getOrderId();

        OrderProduct orderProduct1 = new OrderProduct();
        orderProduct1.setProduct(product1);
        orderProduct1.setOrder(savedOrder);
        orderProduct1.setQuantity(5);
        orderProductRepository.saveAndFlush(orderProduct1);

        OrderProduct orderProduct2 = new OrderProduct();
        orderProduct2.setProduct(product2);
        orderProduct2.setOrder(savedOrder);
        orderProduct2.setQuantity(7);
        orderProductRepository.saveAndFlush(orderProduct2);

        List<OrderProduct> orderProducts = new ArrayList<>();
        orderProducts.add(orderProduct1);
        orderProducts.add(orderProduct2);

        BigDecimal totalPrice = product1.getPrice().multiply(BigDecimal.valueOf(orderProduct1.getQuantity()))
                .add(product2.getPrice().multiply(BigDecimal.valueOf(orderProduct2.getQuantity())));

        savedOrder.setProducts(orderProducts);
        savedOrder.setTotalPrice(totalPrice);
        orderRepository.saveAndFlush(savedOrder);

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
        productRepository.deleteById(product_id_1);
        productRepository.deleteById(product_id_2);
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
                .andDo(result -> logger.info(result.getResponse().getContentAsString()));
    }

    @Test
    @WithMockUser(authorities = "user")
    void createCopyOrder() throws Exception {

        mockMvc.perform(post("/orders/" + order_id + "/create")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(result -> logger.info(result.getResponse().getContentAsString()));
    }

    @Test
    @WithMockUser(authorities = "user")
    void confirmOrderShouldReturnStatus200() throws Exception {
        mockMvc.perform(put("/orders/" + order_id + "/confirm")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(result -> logger.info(result.getResponse().getContentAsString()));

        Optional<Order> updatedOrder = orderRepository.findOrderByOrderId(order_id);
        assert (updatedOrder.isPresent());
        assert (updatedOrder.get().getStatus() == OrderStatus.CONFIRMED);
    }

    @Test
    @WithMockUser(authorities = "user")
    void cancelOrderShouldReturnStatus200() throws Exception{
        mockMvc.perform(put("/orders/" + order_id + "/cancel")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(result -> logger.info(result.getResponse().getContentAsString()));

        Optional<Order> updatedOrder = orderRepository.findOrderByOrderId(order_id);
        assert(updatedOrder.isPresent());
        assert(updatedOrder.get().getStatus() == OrderStatus.CANCELLED);
    }

    @Test
    @WithMockUser(authorities = "user")
    void getOrderShouldReturnOrderWithStatus200() throws Exception{
        mockMvc.perform(get("/orders/" + order_id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(result -> logger.info(result.getResponse().getContentAsString()));

    }

    @Test
    @WithMockUser(authorities = "admin")
    public void generateSalesReportShouldReturnStatus200IncludingReport() throws Exception {

        mockMvc.perform(get("/orders/report")
                        .param("user", String.valueOf(account_id)))
                .andExpect(status().isOk())
                .andDo(result -> logger.info(result.getResponse().getContentAsString()));
    }
}