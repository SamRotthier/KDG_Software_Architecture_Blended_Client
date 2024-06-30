package be.kdg.sa.clients.services;

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
import be.kdg.sa.clients.sender.RestSender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class OrderServiceTest {
    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private OrderProductRepository orderProductRepository;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private AccountService accountService;
    @MockBean
    private RestSender restSender;
    @Autowired
    private OrderService orderService;

    private UUID orderId;
    private UUID accountId;
    private UUID productId1;
    private UUID productId2;
    private UUID orderProductId1;
    private UUID orderProductId2;
    private Account account;
    private Product product1;
    private Product product2;
    private OrderProduct orderProduct1;
    private OrderProduct orderProduct2;
    private Order testOrder;
    private OrderDto orderDto;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        accountId = UUID.randomUUID();
        productId1 = UUID.randomUUID();
        productId2 = UUID.randomUUID();
        orderProductId1 = UUID.randomUUID();
        orderProductId2 = UUID.randomUUID();

        account = new Account(accountId, "testAchter", "testVoor", "test@test.be", "test NV", 0, AccountRelationType.B2B, LoyaltyLevel.BRONZE);

        product1 = new Product(productId1, "Product 1", BigDecimal.TEN, "Description 1", LocalDateTime.now(), ProductState.ACTIVE);
        product2 = new Product(productId2, "Product 2", BigDecimal.valueOf(15), "Description 2", LocalDateTime.now(), ProductState.ACTIVE);

        orderProduct1 = new OrderProduct(orderProductId1, null, product1, 2);
        orderProduct2 = new OrderProduct(orderProductId2, null, product2, 3);

        List<OrderProduct> products = new ArrayList<>();
        products.add(orderProduct1);
        products.add(orderProduct2);

        testOrder = new Order(orderId, products, OrderStatus.PENDING, BigDecimal.valueOf(70), LocalDateTime.now(), account);

        orderProduct1.setOrder(testOrder);
        orderProduct2.setOrder(testOrder);

        given(orderRepository.findOrderByOrderId(orderId)).willReturn(Optional.of(testOrder));

        List<OrderProductDto> productDtos = new ArrayList<>();
        productDtos.add(new OrderProductDto(orderProductId1, orderId, productId1, 2));
        productDtos.add(new OrderProductDto(orderProductId2, orderId, productId2, 3));

        orderDto = new OrderDto(productDtos, OrderStatus.PENDING, accountId);
    }

    @AfterEach
    void tearDown() {
        accountRepository.deleteById(accountId);
        productRepository.deleteById(productId1);
        productRepository.deleteById(productId2);
        orderProductRepository.deleteById(orderProductId1);
        orderProductRepository.deleteById(orderProductId2);
        orderRepository.deleteById(orderId);
    }

    @Test
    public void getOrderByIdShouldReturnCorrectOrder() {
        Optional<Order> order = orderService.getOrderByOrderId(orderId);

        assertTrue(order.isPresent());
        assertEquals(OrderStatus.PENDING, order.get().getStatus());
        assertEquals(BigDecimal.valueOf(70), order.get().getTotalPrice());
        assertEquals(2, order.get().getProducts().size());
        assertEquals(LoyaltyLevel.BRONZE, order.get().getAccount().getLoyaltyLevel());
    }

    @Test
    public void createOrderShouldCalculateTotalPriceAndApplyDiscount() {

        given(accountRepository.findByAccountId(orderDto.getAccountId())).willReturn(new Account());

        System.out.println(orderDto);

        Order createdOrder = orderService.createOrder(orderDto);

        System.out.println(createdOrder);

        // Assert
        assertEquals(LoyaltyLevel.BRONZE, createdOrder.getAccount().getLoyaltyLevel());
        assertEquals(BigDecimal.valueOf(70), createdOrder.getTotalPrice()); // 2*10 + 3*15 = 70
    }
}