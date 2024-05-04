package be.kdg.sa.clients.controller;

import be.kdg.sa.clients.controller.dto.ProductDto;
import be.kdg.sa.clients.domain.Product;
import be.kdg.sa.clients.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/new")
    public ResponseEntity<List<ProductDto>> getNewUnpricedProducts(){
        List<Product> unpricedProducts = productService.getNewUnpricedProducts();

        if(unpricedProducts.isEmpty()){
            return ResponseEntity.noContent().build();
        } else{
            List<ProductDto> unpricedProductDtos = unpricedProducts.stream().map(this::convertToDto).toList();
            return ResponseEntity.ok(unpricedProductDtos);
        }

    }

    private ProductDto convertToDto(Product product){
        ProductDto productDto = new ProductDto(
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getQuantity(),
                product.getCreatedDate(),
                product.getModifiedDate()
        );
        return productDto;
    }
}
