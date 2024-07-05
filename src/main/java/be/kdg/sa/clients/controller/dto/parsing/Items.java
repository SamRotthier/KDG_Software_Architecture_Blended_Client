package be.kdg.sa.clients.controller.dto.parsing;

import jakarta.xml.bind.annotation.XmlElement;

import java.util.Collection;

public class Items {
    private Collection<Item> items;

    public Collection<Item> getItems() {
        return items;
    }

    @XmlElement(name = "Item")
    public void setItems(Collection<Item> items) {
        this.items = items;
    }
}
