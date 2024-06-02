package be.kdg.sa.clients.services;

import be.kdg.sa.clients.controller.dto.ProductDto;
import be.kdg.sa.clients.domain.Enum.ProductState;
import be.kdg.sa.clients.domain.Product;
import be.kdg.sa.clients.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getNewUnpricedProducts() {
        logger.info("Fetching all products with no price.");
        return productRepository.getAllByPriceIsNull();
    }

    public Product setProductPrice(UUID id, Double price) {
        logger.info("Setting price for product with ID {}.", id);
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isPresent()){
            Product product = optionalProduct.get();
            product.setPrice(BigDecimal.valueOf(price));
            logger.info("Price set for product with ID {}: ", id);
            return productRepository.save(product);
        } else {
            logger.error("Product with ID {} was not found.", id);
            return null;
        }
    }

    public void addProductFromMessage(ProductDto productDto) {
        logger.info("Adding new product from message: {}.", productDto);
        Product product = new Product();
        product.setProductId(productDto.getProductId());
        product.setName(productDto.getName());
        product.setProductState(ProductState.INACTIVE);

        productRepository.save(product);
        logger.info("A new product was saved in the db with name: {}", product.getName());
    }

    public void changeProductStateById(UUID productNumber) {
        logger.info("Change state of product with ID {}.", productNumber);
        Optional<Product> optionalProduct = productRepository.getProductByProductId(productNumber);

        if (optionalProduct.isEmpty()) {
            logger.warn("Product with id {} was not found", productNumber);
            return;
        }

        Product product = optionalProduct.get();
        product.setProductState(product.getProductState()==ProductState.ACTIVE ? ProductState.INACTIVE : ProductState.ACTIVE);
        productRepository.save(product);
        logger.info("Product state for ID {} was changed successfully.", productNumber);
    }

    public List<Product> getPricedProducts() {
        logger.info("Fetching all products with price.");
        return productRepository.getAllByPriceIsNotNull();
    }
}
