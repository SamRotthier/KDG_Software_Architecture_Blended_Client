package be.kdg.sa.clients.controller;

import be.kdg.sa.clients.controller.dto.ProductDto;
import be.kdg.sa.clients.domain.Enum.ProductState;
import be.kdg.sa.clients.domain.Product;
import be.kdg.sa.clients.repositories.ProductRepository;
import be.kdg.sa.clients.services.ProductService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ExceptionCollector;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(ProductControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    private UUID product_id;
    private Product product;

    @BeforeEach
    public void setUp(){
        logger.info("Create testdata");
        product_id = UUID.randomUUID();
        ProductDto productDto = new ProductDto(product_id, "test product", LocalDateTime.now());

        product = new Product();
        product.setProductId(productDto.getProductId());
        product.setName(productDto.getName());
        product.setProductState(ProductState.INACTIVE);
        productRepository.save(product);
        logger.info("Test data created: {}", productRepository.findById(product_id));

    }

    @AfterEach
    public void tearDown(){
       productRepository.deleteById(product_id);
    }

    @Test
    @WithMockUser(authorities = "admin")
    public void gettingAllNewUnpricedProductsShouldBeOkForAdmin() throws Exception {
        mockMvc.perform(get("/products/new").accept(APPLICATION_JSON)).andExpect(status().isOk()).andExpect(header().string(CONTENT_TYPE, APPLICATION_JSON.toString())).andDo(result -> logger.info(result.getResponse().getContentAsString()));
    }

   @Test
   @WithMockUser(authorities = "admin")
    void settingProductPriceShouldBeOkForAdmin() throws Exception{
        mockMvc.perform(patch("/products/{product}/price", product_id)
                        .accept(APPLICATION_JSON)
                        .param("price", "3.45"))
                .andExpect(status().isOk())
                .andExpect(header().string(CONTENT_TYPE, APPLICATION_JSON.toString()))
                .andDo(result -> logger.info(result.getResponse().getContentAsString()));
    }

    @Test
    @WithMockUser(authorities = "admin")
    void gettingAllPricedProductsShouldBeOkForAdmin() throws Exception {
        product.setPrice(BigDecimal.TEN);
        productRepository.saveAndFlush(product);
        System.out.println(productRepository.findById(product_id).get().getPrice());
        mockMvc.perform(get("/products/").accept(APPLICATION_JSON)).andExpect(status()
                .isOk()).andExpect(header().string(CONTENT_TYPE, APPLICATION_JSON
                .toString())).andDo(result -> logger.info(result.getResponse()
                .getContentAsString()));

    }
}