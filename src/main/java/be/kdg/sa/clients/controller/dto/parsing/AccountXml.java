package be.kdg.sa.clients.controller.dto.parsing;

import be.kdg.sa.clients.domain.Enum.AccountRelationType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.UUID;

public class AccountXml {
    @XmlAttribute
    private AccountRelationType type = AccountRelationType.B2B;
    @XmlElement
    private String id;

    public AccountRelationType getType() {
        return type;
    }

    public void setType(AccountRelationType type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
