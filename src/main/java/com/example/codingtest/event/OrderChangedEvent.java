package com.example.codingtest.event;

import com.example.codingtest.dto.OrderResponse;
import java.time.LocalDateTime;

public record OrderChangedEvent(
        OrderEventType eventType,
        Long orderId,
        Long userId,
        int totalPrice,
        LocalDateTime occurredAt
) {

    public static OrderChangedEvent created(OrderResponse response) {
        return from(OrderEventType.CREATED, response);
    }

    public static OrderChangedEvent updated(OrderResponse response) {
        return from(OrderEventType.UPDATED, response);
    }

    public static OrderChangedEvent deleted(Long orderId, Long userId, LocalDateTime occurredAt) {
        return new OrderChangedEvent(OrderEventType.DELETED, orderId, userId, 0, occurredAt);
    }

    private static OrderChangedEvent from(OrderEventType eventType, OrderResponse response) {
        return new OrderChangedEvent(
                eventType,
                response.orderId(),
                response.userId(),
                response.totalPrice(),
                LocalDateTime.now()
        );
    }
}
