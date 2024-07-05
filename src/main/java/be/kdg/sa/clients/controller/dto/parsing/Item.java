package be.kdg.sa.clients.controller.dto.parsing;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.UUID;

public class Item {
    @XmlAttribute(name = "ProductNumber")
    private String productNumber;
    @XmlElement(name = "ProductName")
    private String productName;
    @XmlElement(name = "Quantity")
    private Integer quantity;
    @XmlElement(name = "SpecialInstructions")
    private String specialInstructions;


    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

}
