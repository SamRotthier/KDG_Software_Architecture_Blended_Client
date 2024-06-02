package be.kdg.sa.clients.repositories;

import be.kdg.sa.clients.domain.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderProductRepository extends JpaRepository<OrderProduct, UUID> {
}
