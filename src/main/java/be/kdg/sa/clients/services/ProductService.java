package be.kdg.sa.clients.services;

import be.kdg.sa.clients.controller.dto.ProductDto;
import be.kdg.sa.clients.domain.Product;
import be.kdg.sa.clients.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getNewUnpricedProducts() {
        return productRepository.getAllByPriceIsNull();
    }

}
