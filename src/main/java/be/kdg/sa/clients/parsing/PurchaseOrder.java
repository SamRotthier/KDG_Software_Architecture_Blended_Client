package be.kdg.sa.clients.parsing;


import java.io.File;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

public class PurchaseOrder {
    public static <T> T JaxbReadXml(String file, Class<T> typeParamterClass) throws Exception {
        JAXBContext context = JAXBContext.newInstance(typeParamterClass);
        Unmarshaller m = context.createUnmarshaller();
        return (T) m.unmarshal(new File(file));
    }

    public static void JaxbWriteXml(String file, Object root) throws Exception {
        JAXBContext context = JAXBContext.newInstance(root.getClass());
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m.marshal(root, new File(file));
    }
}
