package be.kdg.sa.clients.services;

import be.kdg.sa.clients.controller.dto.OrderDto;
import be.kdg.sa.clients.controller.dto.OrderProductDto;
import be.kdg.sa.clients.domain.Account;
import be.kdg.sa.clients.domain.Enum.LoyaltyLevel;
import be.kdg.sa.clients.domain.Enum.OrderStatus;
import be.kdg.sa.clients.domain.Enum.ProductState;
import be.kdg.sa.clients.domain.Order;
import be.kdg.sa.clients.domain.OrderProduct;
import be.kdg.sa.clients.repositories.AccountRepository;
import be.kdg.sa.clients.repositories.OrderProductRepository;
import be.kdg.sa.clients.repositories.OrderRepository;
import be.kdg.sa.clients.repositories.ProductRepository;
import be.kdg.sa.clients.sender.RestSender;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private RestSender restSender;

    public OrderService(OrderRepository orderRepository, OrderProductRepository orderProductRepository, ProductRepository productRepository, AccountRepository accountRepository, AccountService accountService, RestSender restSender) {
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.productRepository = productRepository;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
        this.restSender = restSender;
    }

    public Optional<Order> getOrderByOrderId(UUID givenOrderId) {
        return orderRepository.findOrderByOrderId(givenOrderId);
    }

    @Transactional
    public Order createOrder(OrderDto orderDto){
        logger.info("Creating new order");

        //get Account
        Account account = accountRepository.findByAccountId(orderDto.getAccountId());
        logger.info(String.valueOf(account));

        //create order
        Order order = new Order();
        order.setOrderId(UUID.randomUUID());
        order.setStatus(OrderStatus.PENDING);
        order.setAccount(account);

        // Filter on active products
        List<OrderProductDto> activeProducts = orderDto.getProducts().stream().filter(p -> isProductActive(p.getProductId())).toList();
        UUID testId = activeProducts.stream().findFirst().orElse(new OrderProductDto()).getProductId();
        if (testId == null) {
            logger.warn("No active products found in the order with ID: {}", orderDto.getOrderId());
        }

        //Total Price
        logger.info("Calculate total price");
        BigDecimal totalPrice = activeProducts.stream().filter(i -> i.getProductId() != null).map(p -> productRepository.findPriceByProductId(p.getProductId()).multiply(BigDecimal.valueOf(p.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);

        //Check discount
        logger.info("Checking and calculating discount");
        order.setLoyaltyLevel(account.getLoyaltyLevel());
        double discount = LoyaltyLevel.getDiscount(account.getPoints());
        if(discount != 0.00){
            BigDecimal totalDiscount = totalPrice.multiply(BigDecimal.valueOf(discount));
            totalPrice.subtract(totalDiscount);
            order.setTotalDiscount(totalDiscount);
        } else{
            order.setTotalDiscount(BigDecimal.ZERO);
        }

        order.setTotalPrice(totalPrice);
        Order savedOrder = orderRepository.save(order);
        logger.info("Order saved with Id: {}", order.getOrderId());

        //Link active products to order
        if(testId != null){
            logger.info("Saving order activeProducts: {}", activeProducts);
            orderProductRepository.saveAll(activeProducts.stream().filter(i -> i.getProductId() != null).map(i -> new OrderProduct(savedOrder, productRepository.findById(i.getProductId()).orElseThrow(), i.getQuantity())).toList());
        }

        //Update account
        int calculatedPoints = totalPrice.divide(BigDecimal.TEN, RoundingMode.DOWN).intValue();
        accountService.updateLoyaltyPointsAndLevel(account.getAccountId(), calculatedPoints);

        return savedOrder;
    }

    @Transactional
    public void confirmOrder(Optional<Order> foundOrder) {
        foundOrder.ifPresent(order -> {
            logger.info("Confirming order with ID: {}", order.getOrderId());
            order.setStatus(OrderStatus.CONFIRMED);
            orderRepository.save(order);
            Order orderMessage = order;
            restSender.sendOrder(orderMessage);
        });
    }

    @Transactional
    public void cancelOrder(Optional<Order> foundOrder) {
        foundOrder.ifPresent(order -> {
            logger.info("Cancelling order with ID: {}", order.getOrderId());
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
        });
    }

    public List<Order> getOrders(){
        logger.info("Fetching all orders");
        return orderRepository.findAll();
    }

    private boolean isProductActive(UUID productId) {
        return productRepository.findById(productId)
                .map(product -> product.getProductState() == ProductState.ACTIVE)
                .orElse(false);
    }

    @Transactional
    public void createCopyOrder(UUID orderId) {
        logger.info("Copying old order");

        Optional<Order> orderToCopy = orderRepository.findOrderByOrderId(orderId);

        //create order
        Order order = new Order();
        order.setOrderId(UUID.randomUUID());
        order.setStatus(OrderStatus.PENDING);
        order.setAccount(orderToCopy.get().getAccount());
        order.setProducts(orderToCopy.get().getProducts()); //TODO Misschien nog nakijken of alle orders nog actief staan
        order.setTotalPrice(orderToCopy.get().getTotalPrice()); //TODO misschien moet dit nagegeken worden aangezien de persoon ineens in een andere korting category kan zitten

        orderRepository.save(order);
    }

    public List<Order> generateSalesReport(UUID productId, LocalDateTime orderDate,UUID accountId){
        List<Order> salesReport = new ArrayList<>();

        if(productId != null){
            logger.info("Generate sales report for product {}", productId);
            salesReport = orderRepository.findAllByProductId();
        } else if(orderDate != null){
            logger.info("Generate sales report for order date {}", orderDate);
            salesReport = orderRepository.findAllByCreationDateTime(orderDate);
        } else if(accountId != null){
            logger.info("Generate sales report for user {}", accountId);
            salesReport = orderRepository.findAllByAccountId(accountId);
        }
        return salesReport;
    }
}
