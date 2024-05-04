package be.kdg.sa.clients.repositories;

import be.kdg.sa.clients.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> getAllByPriceIsNull();
}
