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
        return productRepository.getAllByPriceIsNull();
    }

    public Product setProductPrice(UUID id, Double price) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isPresent()){
            Product product = optionalProduct.get();
            product.setPrice(BigDecimal.valueOf(price));
            return productRepository.save(product);
        } else {
            throw new IllegalArgumentException("Product with ID" + id + "was not found");
        }
    }

    public void addProductFromMessage(ProductDto productDto) {
        Product product = new Product();
        product.setProductId(productDto.getProductId());
        product.setName(productDto.getName());

        productRepository.save(product);
        logger.info("A new product was saved in the db with name: {}", product.getName());
    }

    public void deactivateProductById(UUID productNumber) {
        Optional<Product> optionalProduct = productRepository.getProductByProductId(productNumber);

        if (optionalProduct.isEmpty()) {
            logger.warn("Product with id {} was not found", productNumber);
            return;
        }

        Product product = optionalProduct.get();
        product.setProductState(product.getProductState()==ProductState.ACTIVE ? ProductState.INACTIVE : ProductState.ACTIVE);
        productRepository.save(product);

    }
}
