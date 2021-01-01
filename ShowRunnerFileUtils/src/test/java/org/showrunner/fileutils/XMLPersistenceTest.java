package org.showrunner.fileutils;

import org.showrunner.fileutils.helper.Item;
import org.showrunner.fileutils.helper.Order;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class XMLPersistenceTest {

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order("1", new Date().toString());
        order.addItem(new Item(1, "0001", "Darth Vader ", 25.00));
        order.addItem(new Item(5, "0002", "Stormtrooper", 19.99));
        order.addItem(new Item(1, "0001", "Obi Wan", 39.00));
    }

    @Test
    void persist() {
        XMLPersistence<Order> persistence = new XMLPersistence<>();
        persistence.persist("./testdata/order.xml", order, Order.class);
    }


    @Test
    void restore() {
        XMLPersistence<Order> persistence = new XMLPersistence<>();
        Order restored = persistence.restore("./testdata/order.xml", Order.class);

        assertTrue(order.getItems().size() == restored.getItems().size());
        log.info("ítems = " + restored.getItems().size());

    }

}