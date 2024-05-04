package be.kdg.sa.clients.repositories;

import be.kdg.sa.clients.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
