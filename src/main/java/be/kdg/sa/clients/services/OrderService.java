package be.kdg.sa.clients.services;

import be.kdg.sa.clients.controller.dto.AccountDto;
import be.kdg.sa.clients.controller.dto.OrderDto;
import be.kdg.sa.clients.controller.dto.OrderProductDto;
import be.kdg.sa.clients.domain.Account;
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
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private RestSender restSender;

    public OrderService(OrderRepository orderRepository, OrderProductRepository orderProductRepository, ProductRepository productRepository, AccountRepository accountRepository, RestSender restSender) {
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.productRepository = productRepository;
        this.accountRepository = accountRepository;
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

        List<OrderProductDto> products = orderDto.getProducts();

        //Total Price
        BigDecimal totalPrice = products.stream().filter(i -> i.getId() != null && isProductActive(i.getId())).map(p -> productRepository.findPriceByProductId(p.getId()).multiply(BigDecimal.valueOf(p.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalPrice(totalPrice);
        Order savedOrder = orderRepository.save(order);
        logger.info("Order saved with Id: {}", order.getOrderId());

        //Link products to order
        if(products != null){
            logger.info("Saving order products: {}", products);
            orderProductRepository.saveAll(products.stream().filter(i -> i.getId() != null && isProductActive(i.getId())).map(i -> new OrderProduct(savedOrder, productRepository.findById(i.getId()).orElseThrow(), i.getQuantity())).toList());
        }

        return savedOrder;
    }

    public void confirmOrder(Optional<Order> foundOrder) {
        foundOrder.ifPresent(order -> {
            logger.info("Confirming order with ID: {}", order.getOrderId());
            order.setStatus(OrderStatus.CONFIRMED);
            orderRepository.save(order);
            Order orderMessage = order;
            restSender.sendOrder(orderMessage);
        });
    }

    public void cancelOrder(Optional<Order> foundOrder) {
        foundOrder.ifPresent(order -> {
            logger.info("Cancelling order with ID: {}", order.getOrderId());
            orderRepository.save(order);
            order.setStatus(OrderStatus.CANCELLED);
        });
    }

    public Optional<List<Order>> getOrders(){
        logger.info("Fetching all orders");
        return Optional.of(orderRepository.findAll());
    }

    private boolean isProductActive(UUID productId) {
        return productRepository.findById(productId)
                .map(product -> product.getProductState() == ProductState.ACTIVE)
                .orElse(false);
    }

}
