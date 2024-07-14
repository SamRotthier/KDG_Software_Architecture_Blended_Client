package be.kdg.sa.clients.controller.dto.parsing;

import be.kdg.sa.clients.domain.Account;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Collection;

@XmlRootElement(name = "PurchaseOrder")
@XmlAccessorType(XmlAccessType.FIELD)
public class PurchaseOrder {
    private Collection<Items> items;

    private AccountXml account;

    public Collection<Items> getItems() {
        return items;
    }

    public AccountXml getAccount() {
        return account;
    }

    @XmlElement(name = "Items")
    public void setItems(Collection<Items> items) {
        this.items = items;
    }
}
