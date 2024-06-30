package be.kdg.sa.clients.services;

import be.kdg.sa.clients.controller.dto.ProductDto;
import be.kdg.sa.clients.domain.Enum.ProductState;
import be.kdg.sa.clients.domain.Product;
import be.kdg.sa.clients.repositories.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class ProductServiceTest {

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    private Product product;
    private UUID productId;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        product = new Product(productId, "Test Product", null, "Description", now, ProductState.INACTIVE);

        productDto = new ProductDto(productId, "Test Product Dto", now);
    }

    @AfterEach
    void tearDown(){
        productRepository.deleteById(productId);
    }

    @Test
    void shouldFetchAllProductsWithNoPrice() {
        given(productRepository.getAllByPriceIsNull()).willReturn(List.of(product));

        List<Product> products = productService.getNewUnpricedProducts();

        assertNotNull(products);
        assertFalse(products.isEmpty());
        assertEquals(1, products.size());
    }

    @Test
    void shouldSetPriceForExistingProduct() {
        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        given(productRepository.save(product)).willReturn(product);

        Product updatedProduct = productService.setProductPrice(productId, 100.0);

        assertNotNull(updatedProduct);
        assertEquals(BigDecimal.valueOf(100.0), updatedProduct.getPrice());
        assertTrue(productRepository.findById(productId).isPresent());
    }

    @Test
    void shouldNotSetPriceForNonExistingProduct() {
        UUID nonExistingProductId = UUID.randomUUID();
        given(productRepository.findById(nonExistingProductId)).willReturn(Optional.empty());

        Product updatedProduct = productService.setProductPrice(nonExistingProductId, 100.0);

        assertNull(updatedProduct, "Updated product should be null when the product does not exist in the repository");
    }

    @Test
    void shouldAddProductFromMessage() {
        productService.addProductFromMessage(productDto);

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository, times(1)).save(productCaptor.capture());

        Product savedProduct = productCaptor.getValue();
        assertNotNull(savedProduct);
        assertEquals(productDto.getProductId(), savedProduct.getProductId());
        assertEquals(productDto.getName(), savedProduct.getName());
        assertEquals(ProductState.INACTIVE, savedProduct.getProductState());
    }

    @Test
    void shouldChangeProductStateById() {
        product.setProductState(ProductState.INACTIVE);
        given(productRepository.getProductByProductId(productId)).willReturn(Optional.of(product));
        given(productRepository.save(product)).willReturn(product);

        productService.changeProductStateById(productId);

        assertEquals(ProductState.ACTIVE, product.getProductState());
    }

    @Test
    void shouldNotChangeProductStateForNonExistingProduct() {
        UUID nonExistingProductId = UUID.randomUUID();
        given(productRepository.getProductByProductId(nonExistingProductId)).willReturn(Optional.empty());

        productService.changeProductStateById(nonExistingProductId);
        assertEquals(ProductState.INACTIVE, product.getProductState());
    }

    @Test
    void shouldFetchAllProductsWithPrice() {
        product.setPrice(BigDecimal.valueOf(100.0));
        given(productRepository.getAllByPriceIsNotNull()).willReturn(List.of(product));

        List<Product> products = productService.getPricedProducts();

        assertNotNull(products);
        assertFalse(products.isEmpty());
        assertEquals(1, products.size());
    }
}