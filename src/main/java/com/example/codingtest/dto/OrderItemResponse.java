package com.example.codingtest.dto;

import com.example.codingtest.domain.OrderItem;
import java.io.Serializable;

public record OrderItemResponse(
        Long id,
        String productName,
        int price,
        int quantity,
        int totalPrice
) implements Serializable {
    public static OrderItemResponse from(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getProductName(),
                item.getPrice(),
                item.getQuantity(),
                item.getTotalPrice()
        );
    }
}
