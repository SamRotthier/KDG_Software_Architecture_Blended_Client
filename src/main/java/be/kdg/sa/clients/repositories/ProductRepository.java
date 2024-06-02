package be.kdg.sa.clients.repositories;

import be.kdg.sa.clients.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> getAllByPriceIsNull();
    Optional<Product> getProductByProductId(UUID uuid);
    @Query("SELECT p.price FROM Product p WHERE p.productId = :productId")
    BigDecimal findPriceByProductId(UUID productId);

    List<Product> getAllByPriceIsNotNull();
}
