package com.example.codingtest.dto;

import com.example.codingtest.domain.Order;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long orderId,
        Long userId,
        List<OrderItemResponse> items,
        int totalPrice,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements Serializable {
    public static OrderResponse from(Order order) {
        List<OrderItemResponse> items = order.getItems()
                .stream()
                .map(OrderItemResponse::from)
                .toList();
        int totalPrice = items.stream()
                .mapToInt(OrderItemResponse::totalPrice)
                .sum();

        return new OrderResponse(
                order.getOrderId(),
                order.getUserId(),
                items,
                totalPrice,
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
