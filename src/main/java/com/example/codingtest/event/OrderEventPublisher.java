package com.example.codingtest.event;

public interface OrderEventPublisher {

    void publish(OrderChangedEvent event);
}
