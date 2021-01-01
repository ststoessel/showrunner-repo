package org.showrunner.fileutils.helper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@XmlRootElement(name = "order")
@XmlType(propOrder = {"orderNumber", "orderDate", "items"})
@XmlAccessorType(XmlAccessType.FIELD)
public class Order {

    @Getter @Setter
    private String orderNumber;

    @Getter @Setter
    private String orderDate;

    @Getter @Setter
    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    private List<Item> items = new ArrayList<>();

    public Order(String orderNumber, String orderDate) {
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
    }

    public void addItem(Item item) {
        items.add(item);
    }

}
