package be.kdg.sa.clients.parsing;

import be.kdg.sa.clients.domain.OrderProduct;
import be.kdg.sa.clients.repositories.AccountRepository;
import be.kdg.sa.clients.repositories.OrderProductRepository;
import be.kdg.sa.clients.repositories.OrderRepository;
import be.kdg.sa.clients.repositories.ProductRepository;
import be.kdg.sa.clients.services.OrderService;
import be.kdg.sa.clients.services.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class PurchaseOrderParsing {

    @Test
    public void testJaxb() throws Exception {
        OrderProduct orderProductsFromFile = PurchaseOrder.JaxbReadXml("./ExamplePurchaseOrder.xml", OrderProduct.class);
        //PurchaseOrder.JaxbWriteXml("./ExamplePurchaseOrder_Test.xml", OrderProductToTest);
        //assertArrayEquals(orderProducts.sortedOnName().toArray(), OrderProductToTest.sortedOnName().toArray(), "the xml file and list should be the same (Jaxb)");
        assertNotEquals("", orderProductsFromFile);
    }
}