package be.kdg.sa.clients.util.parsing;

import be.kdg.sa.clients.controller.dto.parsing.PurchaseOrder;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class OrderParserJaxb {
    public PurchaseOrder read(InputStream stream) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(PurchaseOrder.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (PurchaseOrder) unmarshaller.unmarshal(stream);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
