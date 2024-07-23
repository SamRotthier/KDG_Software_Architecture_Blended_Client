package be.kdg.sa.clients.controller.dto.parsing;

import be.kdg.sa.clients.domain.Enum.AccountRelationType;
import jakarta.xml.bind.annotation.*;

import java.util.UUID;

@XmlAccessorType(XmlAccessType.FIELD)
public class AccountXml {
    @XmlAttribute(name = "Type")
    private AccountRelationType type = AccountRelationType.B2B;
    @XmlValue
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
