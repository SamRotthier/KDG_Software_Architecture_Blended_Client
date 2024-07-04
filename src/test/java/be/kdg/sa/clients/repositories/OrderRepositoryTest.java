package be.kdg.sa.clients.repositories;

import be.kdg.sa.clients.domain.Account;
import be.kdg.sa.clients.domain.Order;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderRepositoryTest {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    AccountRepository accountRepository;

    private Order order;
    private UUID accountId;


    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();
        Account account = new Account();
        account.setAccountId(accountId);
        account = accountRepository.save(account);

        order = new Order();
        order.setOrderId(UUID.randomUUID());
        order.setAccount(account);
        order.setCreationDateTime(LocalDateTime.now());
        order = orderRepository.save(order);
    }

    @Test
    public void FindOrderByOrderIdShouldReturnOrder() {
        Optional<Order> foundOrder = orderRepository.findOrderByOrderId(order.getOrderId());
        assertTrue(foundOrder.isPresent());
        assertTrue(foundOrder.get().getOrderId().equals(order.getOrderId()));
    }

    @Test
    public void FindAllByCreationDateTimeShouldReturnOrdersForDateTime() {
        List<Order> orders = orderRepository.findAllByCreationDateTime(order.getCreationDateTime());
        assertTrue(orders.size() > 0);
        assertTrue(orders.get(0).getCreationDateTime().equals(order.getCreationDateTime()));
    }
}
