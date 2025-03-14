package be.kdg.sa.clients.controller;

import be.kdg.sa.clients.controller.dto.ProductDto;
import be.kdg.sa.clients.domain.Product;
import be.kdg.sa.clients.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize("hasAnyAuthority('admin')")
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
    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/")
    public ResponseEntity<List<ProductDto>> getPricedProducts(){
        List<Product> pricedProducts = productService.getPricedProducts();
        if(pricedProducts.isEmpty()){
            return ResponseEntity.noContent().build();
        } List<ProductDto> pricedProductList = pricedProducts.stream().map(this::convertToDto).toList();
        return ResponseEntity.ok(pricedProductList);
    }
    @PreAuthorize("hasAnyAuthority('admin')")
    @PatchMapping("/{productId}/price")
    public ResponseEntity<ProductDto> setProductPrice(@Valid @PathVariable("productId") UUID id, @RequestParam Double price){
        Product product = productService.setProductPrice(id, price);
        if(product != null){
            return ResponseEntity.ok(convertToDto(product));
        } else{
            return ResponseEntity.notFound().build();
        }
    }

    private ProductDto convertToDto(Product product){
        return new ProductDto(
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                product.getCreatedDate()
        );
    }
}
