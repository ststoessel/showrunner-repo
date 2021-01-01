package org.showrunner.fileutils.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "item")
@XmlType(propOrder = {"quantity", "id", "name", "price"})
public class Item {

    @Getter
    @Setter
    private int quantity;
    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private double price;

}
