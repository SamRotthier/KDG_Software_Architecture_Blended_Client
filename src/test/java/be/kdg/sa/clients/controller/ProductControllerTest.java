package be.kdg.sa.clients.controller;

import be.kdg.sa.clients.controller.dto.ProductDto;
import be.kdg.sa.clients.domain.Enum.ProductState;
import be.kdg.sa.clients.domain.Product;
import be.kdg.sa.clients.repositories.ProductRepository;
import be.kdg.sa.clients.services.ProductService;
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
import org.springframework.test.util.ExceptionCollector;
import org.springframework.test.web.servlet.MockMvc;
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
class ProductControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(ProductControllerTest.class);

    private static final UUID product_id = UUID.randomUUID();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp(){
        logger.info("Create testdata");
        ProductDto productDto = new ProductDto(product_id, "test product", LocalDateTime.now());

        Product product = new Product();
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
    //@WithMockUser(authorities = "admin") (vergeet uw dependency niet toe te voegen)
    public void gettingAllNewUnpricedProductsShouldBeOkForAdmin() throws Exception {
        mockMvc.perform(get("/products/new").accept(APPLICATION_JSON)).andExpect(status().isOk()).andExpect(header().string(CONTENT_TYPE, APPLICATION_JSON.toString())).andDo(result -> logger.info(result.getResponse().getContentAsString()));
    }

   @Test
        //@WithMockUser(authorities = "admin") (vergeet uw dependency niet toe te voegen)
    void settingProductPriceShouldBeOkForAdmin() throws Exception{
        mockMvc.perform(patch("/products/{product}/price", product_id)
                        .accept(APPLICATION_JSON)
                        .param("price", "3.45"))
                .andExpect(status().isOk())
                .andExpect(header().string(CONTENT_TYPE, APPLICATION_JSON.toString()))
                .andDo(result -> logger.info(result.getResponse().getContentAsString()));
    }

    @Test
        //@WithMockUser(authorities = "admin") (vergeet uw dependency niet toe te voegen)
    void gettingAllPricedProductsShouldBeOkForAdmin() throws Exception {
        mockMvc.perform(get("/").accept(APPLICATION_JSON)).andExpect(status()
                .isOk()).andExpect(header().string(CONTENT_TYPE, APPLICATION_JSON
                .toString())).andDo(result -> logger.info(result.getResponse()
                .getContentAsString()));

    }
}