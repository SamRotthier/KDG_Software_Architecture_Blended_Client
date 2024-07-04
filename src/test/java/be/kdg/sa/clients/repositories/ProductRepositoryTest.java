package be.kdg.sa.clients.repositories;

import be.kdg.sa.clients.domain.Product;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductRepositoryTest {
    @Autowired
    ProductRepository productRepository;
    @BeforeEach
    void setUp() {
    }

    @Test
    void GetAllByPriceIsNullShouldRetrieveAllProductsWithoutPrice() {
        Product product1 = new Product();
        UUID productId1 = UUID.randomUUID();
        product1.setProductId(productId1);
        product1.setPrice(null);
        productRepository.save(product1);

        Product product2 = new Product();
        UUID productId2 = UUID.randomUUID();
        product2.setProductId(productId2);
        product2.setPrice(BigDecimal.valueOf(10.0));
        productRepository.save(product2);

        List<Product> products = productRepository.getAllByPriceIsNull();
        assertEquals(1, products.size());
        assertEquals(productId1, products.get(0).getProductId());
    }

    @Test
    void GetProductByProductIdShouldRetrieveExactProduct() {
        Product product = new Product();
        UUID productId = UUID.randomUUID();
        product.setProductId(productId);
        product.setPrice(BigDecimal.valueOf(20.0));
        Product savedProduct = productRepository.save(product);

        Optional<Product> foundProduct = productRepository.getProductByProductId(savedProduct.getProductId());
        assertTrue(foundProduct.isPresent());
        assertEquals(savedProduct, foundProduct.get());
    }

    @Test
    void FindPriceByProductIdShouldRetrievePrice() {
        Product product = new Product();
        UUID productId = UUID.randomUUID();
        product.setProductId(productId);
        product.setPrice(BigDecimal.valueOf(30.00));
        Product savedProduct = productRepository.save(product);

        BigDecimal expectedPrice = BigDecimal.valueOf(30.0).setScale(2, RoundingMode.HALF_UP);
        BigDecimal price = productRepository.findPriceByProductId(savedProduct.getProductId());
        assertEquals(expectedPrice, price);
    }
}