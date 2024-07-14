package be.kdg.sa.clients.controller.dto.parsing;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.Collection;

@XmlAccessorType(XmlAccessType.FIELD)
public class Items {
    @XmlElement(name = "Item")
    private Collection<Item> items;

    public Collection<Item> getItems() {
        return items;
    }

    @XmlElement(name = "Item")
    public void setItems(Collection<Item> items) {
        this.items = items;
    }
}
