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
import org.mockito.ArgumentCaptor;
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
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
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

        testOrder = new Order(orderId, products, OrderStatus.PENDING, BigDecimal.valueOf(65), LocalDateTime.now(), account);

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
        assertEquals(BigDecimal.valueOf(65), order.get().getTotalPrice());
        assertEquals(2, order.get().getProducts().size());
        assertEquals(LoyaltyLevel.BRONZE, order.get().getAccount().getLoyaltyLevel());
    }

    @Test
    public void createOrderShouldCreateNewOrder() {
        given(accountRepository.findByAccountId(accountId)).willReturn(account);
        given(productRepository.findById(productId1)).willReturn(Optional.of(product1));
        given(productRepository.findById(productId2)).willReturn(Optional.of(product2));
        given(productRepository.findPriceByProductId(productId1)).willReturn(BigDecimal.TEN);
        given(productRepository.findPriceByProductId(productId2)).willReturn(BigDecimal.valueOf(15));
        given(orderRepository.save(any(Order.class))).willReturn(testOrder);

        Order createdOrder = orderService.createOrder(orderDto);

        assertNotNull(createdOrder);
        assertEquals(OrderStatus.PENDING, createdOrder.getStatus());
        assertEquals(account, createdOrder.getAccount());
        assertEquals(BigDecimal.valueOf(65), createdOrder.getTotalPrice());
    }

    @Test
    public void confirmOrderShouldChangeStatusToConfirmed() {
        given(orderRepository.save(any(Order.class))).willReturn(testOrder);

        orderService.confirmOrder(Optional.of(testOrder));

        assertEquals(OrderStatus.CONFIRMED, testOrder.getStatus());
    }

    @Test
    public void confirmOrderShouldNotChangeStatusIfOrderNotFound() {
        orderService.confirmOrder(Optional.empty());

        assertEquals(OrderStatus.PENDING, testOrder.getStatus()); // Status should remain unchanged
    }

    @Test
    public void cancelOrderShouldChangeStatusToCancelled() {
        given(orderRepository.save(any(Order.class))).willReturn(testOrder);

        orderService.cancelOrder(Optional.of(testOrder));

        assertEquals(OrderStatus.CANCELLED, testOrder.getStatus());
    }

    @Test
    public void cancelOrderShouldNotChangeStatusIfOrderNotFound() {
        orderService.cancelOrder(Optional.empty());

        assertEquals(OrderStatus.PENDING, testOrder.getStatus()); // Status should remain unchanged
    }

    @Test
    public void getOrdersShouldReturnAllOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(testOrder);
        given(orderRepository.findAll()).willReturn(orders);

        List<Order> allOrders = orderService.getOrders();

        assertEquals(1, allOrders.size());
        assertEquals(testOrder, allOrders.get(0));
    }

    @Test
    public void isProductActiveShouldReturnTrueForActiveProduct() {
        given(productRepository.findById(productId1)).willReturn(Optional.of(product1));

        boolean isActive = orderService.isProductActive(productId1);

        assertTrue(isActive);
    }

    @Test
    public void isProductActiveShouldReturnFalseForInactiveProduct() {
        Product inactiveProduct = new Product(productId1, "Inactive Product", BigDecimal.TEN, "Description", LocalDateTime.now(), ProductState.INACTIVE);
        given(productRepository.findById(productId1)).willReturn(Optional.of(inactiveProduct));

        boolean isActive = orderService.isProductActive(productId1);

        assertFalse(isActive);
    }

    @Test
    public void isProductActiveShouldReturnFalseForNonExistentProduct() {
        given(productRepository.findById(productId1)).willReturn(Optional.empty());

        boolean isActive = orderService.isProductActive(productId1);

        assertFalse(isActive);
    }

    @Test
    public void createOrderShouldReturnNullWhenNoActiveProductsFound() {
        product1.setProductState(ProductState.INACTIVE);
        List<OrderProductDto> inactiveProductDtos = new ArrayList<>();
        inactiveProductDtos.add(new OrderProductDto(orderProductId1, orderId, productId1, 2));

        OrderDto inactiveOrderDto = new OrderDto(inactiveProductDtos, OrderStatus.PENDING, accountId);
        given(accountRepository.findByAccountId(accountId)).willReturn(account);
        given(productRepository.findById(productId1)).willReturn(Optional.of(new Product(productId1, "Inactive Product", BigDecimal.TEN, "Description", LocalDateTime.now(), ProductState.INACTIVE)));

        Order createdOrder = orderService.createOrder(inactiveOrderDto);
        assertNull(createdOrder);
    }

    @Test
    public void createOrderShouldApplyDiscountWhenEligible() {
        account.setPoints(10000);
        given(accountRepository.findByAccountId(accountId)).willReturn(account);
        given(productRepository.findById(productId1)).willReturn(Optional.of(product1));
        given(productRepository.findById(productId2)).willReturn(Optional.of(product2));
        given(productRepository.findPriceByProductId(productId1)).willReturn(BigDecimal.TEN);
        given(productRepository.findPriceByProductId(productId2)).willReturn(BigDecimal.valueOf(15));
        given(orderRepository.save(any(Order.class))).willReturn(testOrder);

        Order createdOrder = orderService.createOrder(orderDto);

        BigDecimal expectedDiscount = BigDecimal.valueOf(65).multiply(BigDecimal.valueOf(LoyaltyLevel.getDiscount(account.getPoints())));
        BigDecimal expectedTotalPrice = BigDecimal.valueOf(65).subtract(expectedDiscount);

        System.out.println(expectedDiscount);
        System.out.println(expectedTotalPrice);
        System.out.println(createdOrder.getTotalDiscount());
        System.out.println(createdOrder.getTotalPrice());

        assertEquals(expectedTotalPrice, createdOrder.getTotalPrice());
        assertEquals(expectedDiscount, createdOrder.getTotalDiscount());
    }


    @Test
    public void createCopyOrderShouldCopyExistingOrder() {
        given(orderRepository.findOrderByOrderId(orderId)).willReturn(Optional.of(testOrder));
        ArgumentCaptor<Order> orderCaptor = forClass(Order.class);
        given(orderRepository.save(orderCaptor.capture())).willAnswer(invocation -> invocation.getArgument(0));

        orderService.createCopyOrder(orderId);

        Order copiedOrder = orderCaptor.getValue();
        assertNotNull(copiedOrder);
        assertNotEquals(testOrder.getOrderId(), copiedOrder.getOrderId());
        assertEquals(OrderStatus.PENDING, copiedOrder.getStatus());
        assertEquals(testOrder.getAccount(), copiedOrder.getAccount());
        assertEquals(testOrder.getProducts(), copiedOrder.getProducts());
        assertEquals(testOrder.getTotalPrice(), copiedOrder.getTotalPrice());
    }
}