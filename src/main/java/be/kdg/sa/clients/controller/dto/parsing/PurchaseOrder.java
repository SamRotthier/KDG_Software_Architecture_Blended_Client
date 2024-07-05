package be.kdg.sa.clients.controller.dto.parsing;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Collection;

@XmlRootElement(name = "PurchaseOrder")
public class PurchaseOrder {
    private Collection<Items> items;

    public Collection<Items> getItems() {
        return items;
    }

    @XmlElement(name = "Items")
    public void setItems(Collection<Items> items) {
        this.items = items;
    }
}
