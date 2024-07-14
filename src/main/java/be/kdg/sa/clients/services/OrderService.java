package be.kdg.sa.clients.services;

import be.kdg.sa.clients.controller.dto.OrderDto;
import be.kdg.sa.clients.controller.dto.OrderProductDto;
import be.kdg.sa.clients.controller.dto.ProductSalesDto;
import be.kdg.sa.clients.controller.dto.parsing.Item;
import be.kdg.sa.clients.controller.dto.parsing.Items;
import be.kdg.sa.clients.controller.dto.parsing.PurchaseOrder;
import be.kdg.sa.clients.domain.Account;
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
import be.kdg.sa.clients.util.parsing.OrderParserJaxb;
import jakarta.transaction.Transactional;
import org.hibernate.engine.internal.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final OrderParserJaxb orderParserJaxb;
    private RestSender restSender;


    public OrderService(OrderRepository orderRepository, OrderProductRepository orderProductRepository, ProductRepository productRepository, AccountRepository accountRepository, AccountService accountService, OrderParserJaxb orderParserJaxb, RestSender restSender) {
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.productRepository = productRepository;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
        this.orderParserJaxb = orderParserJaxb;
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
            return null;
        }

        //Total Price
        logger.info("Calculate total price");
        BigDecimal totalPrice = activeProducts.stream().filter(i -> i.getProductId() != null).map(p -> productRepository.findPriceByProductId(p.getProductId()).multiply(BigDecimal.valueOf(p.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);
        logger.info("Total price: {}", totalPrice);
        //Check discount
        logger.info("Checking and calculating discount");
        order.setLoyaltyLevel(account.getLoyaltyLevel());
        double discount = LoyaltyLevel.getDiscount(account.getPoints());
        logger.info("Discount calculated: {}", discount);
        if(discount != 0.00){
            BigDecimal totalDiscount = totalPrice.multiply(BigDecimal.valueOf(discount));
            BigDecimal totalPriceDiscounted = totalPrice.subtract(totalDiscount);
            order.setTotalDiscount(totalDiscount);
            order.setTotalPrice(totalPriceDiscounted);
            logger.info("Total price after discount: {}", totalPriceDiscounted);
        } else{
            order.setTotalDiscount(BigDecimal.ZERO);
            order.setTotalPrice(totalPrice);
            logger.info("Total price without discount: {}", totalPrice);
        }

        orderRepository.save(order);
        logger.info("Order saved with Id: {}", order.getOrderId());

        //Link active products to order
        if(testId != null){
            logger.info("Saving order activeProducts: {}", activeProducts);
            List<OrderProduct> orderProducts = activeProducts.stream().filter(i -> i.getProductId() != null).map(i -> {
                Product product = productRepository.findById(i.getProductId()).orElseThrow();
                product.setOrderCounter(product.getOrderCounter() + i.getQuantity());
                return new OrderProduct(order, product, i.getQuantity());
            }).toList();
            orderProductRepository.saveAll(orderProducts);
        }

        //Update account
        int calculatedPoints = totalPrice.divide(BigDecimal.TEN, RoundingMode.DOWN).intValue();
        accountService.updateLoyaltyPointsAndLevel(account.getAccountId(), calculatedPoints);

        return order;
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

    public boolean isProductActive(UUID productId) {
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
        order.setProducts(orderToCopy.get().getProducts());
        order.setTotalPrice(orderToCopy.get().getTotalPrice());
        orderRepository.save(order);
    }

    public List<?> generateSalesReport(UUID productId, LocalDateTime orderDate,UUID accountId) {
        List<?> salesReport = new ArrayList<>();

        if (productId != null) {
            logger.info("Generate sales report for product {}", productId);
            Optional<Product> productOptional = productRepository.getProductByProductId(productId);
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                salesReport = Stream.of(new ProductSalesDto(productId, product.getName(), product.getPrice(), product.getOrderCounter())).toList();
            }
        } else if (orderDate != null) {
            logger.info("Generate sales report for order date {}", orderDate);
            salesReport = orderRepository.findAllByCreationDateTime(orderDate);
        } else if (accountId != null) {
            logger.info("Generate sales report for user {}", accountId);
            salesReport = orderRepository.findAllByAccountId(accountId);
        } else if (productId == null || orderDate == null || accountId == null) {
            return null;
        }
        return salesReport;
    }

    public void PurchaseOrderWithXML(InputStream stream) throws IOException {
        logger.info("Start XML stream");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        stream.transferTo(baos);
        InputStream clone = new ByteArrayInputStream(baos.toByteArray());
        final PurchaseOrder purchaseOrder = orderParserJaxb.read(clone);

        Collection<Items> items = purchaseOrder.getItems();
        // Map PurchaseOrder to OrderDto

        for (Items item: items) {
            Collection<Item> itemCollection = item.getItems();
            logger.info("Save products to orderProductList");

            List<OrderProductDto> orderProducts = itemCollection.stream()
                    .map(i -> new OrderProductDto(
                            null,
                            UUID.fromString(i.getProductNumber()),
                            i.getQuantity())).toList();
            logger.info("Make order ready for creation");
            OrderDto orderDto = new OrderDto(orderProducts, UUID.fromString(purchaseOrder.getAccount().getId()));
            createOrder(orderDto);
        }
    }
}
