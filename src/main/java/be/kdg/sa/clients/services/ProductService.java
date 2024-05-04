package be.kdg.sa.clients.services;

import be.kdg.sa.clients.controller.dto.ProductDto;
import be.kdg.sa.clients.domain.Product;
import be.kdg.sa.clients.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

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
}
