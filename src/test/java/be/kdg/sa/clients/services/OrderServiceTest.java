package be.kdg.sa.clients.services;

import be.kdg.sa.clients.controller.dto.OrderDto;
import be.kdg.sa.clients.controller.dto.OrderProductDto;
import be.kdg.sa.clients.domain.Enum.OrderStatus;
import be.kdg.sa.clients.domain.Order;
import be.kdg.sa.clients.domain.OrderProduct;
import be.kdg.sa.clients.domain.Product;
import be.kdg.sa.clients.repositories.AccountRepository;
import be.kdg.sa.clients.repositories.OrderProductRepository;
import be.kdg.sa.clients.repositories.OrderRepository;
import be.kdg.sa.clients.repositories.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceTest.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(){

    }

    @AfterEach
    public void tearDown(){

    }

    @Test
    public void creatingOrderShouldBeSavedCorrectlyToDB{

        UUID orderUuid = UUID.randomUUID();
        OrderProductDto product1 = new OrderProductDto();
        OrderProduct orderProduct1 = new OrderProduct();

        product1.setId(UUID.randomUUID());
        product1.setOrderId(orderUuid);
        product1.setProductId(UUID.randomUUID());
        product1.setQuantity(50);

        List<OrderProductDto> allProducts = new ArrayList<>();
        allProducts.add(product1);

        List<OrderProduct> allOrderProducts = new ArrayList<>();
        allOrderProducts.add(orderProduct1);

        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(orderUuid);
        orderDto.setStatus(OrderStatus.PENDING);
        orderDto.setProducts(allProducts);
        orderDto.setTotalPrice(10.00);

        Order order = new Order();
        order.setOrderId(orderUuid);
        order.setStatus(OrderStatus.PENDING);
        order.setProducts(allOrderProducts);
        order.setTotalPrice(BigDecimal.valueOf(10.00));

        orderService.createOrder(orderDto);

        Optional<Order> dbOptionalOrder = orderService.getOrderByOrderId(orderUuid);
        Order dbOrder= dbOptionalOrder.get();
        assertEquals(dbOrder, order);
    }


    @Test
    void createOrderIncludingCalculaltingTheTotalPriceShouldSucceed() {
    }

    @Test
    void calculateTotalPriceTest(){

    }
}