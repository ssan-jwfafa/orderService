package com.example.codingtest.event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "app.kafka", name = "enabled", havingValue = "true")
public class KafkaOrderEventPublisher implements OrderEventPublisher {

    private final KafkaTemplate<String, OrderChangedEvent> kafkaTemplate;
    private final String orderTopic;

    public KafkaOrderEventPublisher(
            KafkaTemplate<String, OrderChangedEvent> kafkaTemplate,
            @Value("${app.kafka.order-topic}") String orderTopic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.orderTopic = orderTopic;
    }

    @Override
    public void publish(OrderChangedEvent event) {
        kafkaTemplate.send(orderTopic, event.orderId().toString(), event);
    }
}
