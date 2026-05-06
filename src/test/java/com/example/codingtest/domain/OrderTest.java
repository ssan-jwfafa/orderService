package com.example.codingtest.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void addItemAssignsOrderAndKeepsItemSnapshotReadOnly() {
        Order order = new Order(1L);
        OrderItem item = new OrderItem("apple", 1000, 2);

        order.addItem(item);

        assertThat(order.getItems()).containsExactly(item);
        assertThat(order.getItems()).isUnmodifiable();
    }

    @Test
    void updateReplacesUserAndItems() {
        Order order = new Order(1L);
        order.addItem(new OrderItem("apple", 1000, 2));

        OrderItem orange = new OrderItem("orange", 700, 4);
        order.update(2L, List.of(orange));

        assertThat(order.getUserId()).isEqualTo(2L);
        assertThat(order.getItems()).containsExactly(orange);
    }
}
