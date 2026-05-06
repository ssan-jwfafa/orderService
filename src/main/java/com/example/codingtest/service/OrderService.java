package com.example.codingtest.service;

import com.example.codingtest.domain.Order;
import com.example.codingtest.domain.OrderItem;
import com.example.codingtest.dto.OrderCreateRequest;
import com.example.codingtest.dto.OrderItemRequest;
import com.example.codingtest.dto.OrderResponse;
import com.example.codingtest.dto.OrderUpdateRequest;
import com.example.codingtest.event.OrderChangedEvent;
import com.example.codingtest.event.OrderEventPublisher;
import com.example.codingtest.exception.OrderNotFoundException;
import com.example.codingtest.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventPublisher orderEventPublisher;

    public OrderService(OrderRepository orderRepository, OrderEventPublisher orderEventPublisher) {
        this.orderRepository = orderRepository;
        this.orderEventPublisher = orderEventPublisher;
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::from)
                .toList();
    }

    @Cacheable(cacheNames = "orders", key = "#id")
    public OrderResponse findById(Long id) {
        return OrderResponse.from(getOrder(id));
    }

    @Transactional
    @CachePut(cacheNames = "orders", key = "#result.orderId()")
    public OrderResponse create(OrderCreateRequest request) {
        Order order = new Order(request.userId());
        request.items().stream()
                .map(this::toOrderItem)
                .forEach(order::addItem);

        OrderResponse response = OrderResponse.from(orderRepository.save(order));
        publishAfterCommit(OrderChangedEvent.created(response));
        return response;
    }

    @Transactional
    @CachePut(cacheNames = "orders", key = "#id")
    public OrderResponse update(Long id, OrderUpdateRequest request) {
        Order order = getOrder(id);
        order.update(
                request.userId(),
                request.items().stream()
                        .map(this::toOrderItem)
                        .toList()
        );
        orderRepository.flush();
        OrderResponse response = OrderResponse.from(order);
        publishAfterCommit(OrderChangedEvent.updated(response));
        return response;
    }

    @Transactional
    @CacheEvict(cacheNames = "orders", key = "#id")
    public void delete(Long id) {
        Order order = getOrder(id);
        OrderChangedEvent event = OrderChangedEvent.deleted(
                order.getOrderId(),
                order.getUserId(),
                LocalDateTime.now()
        );
        orderRepository.delete(order);
        publishAfterCommit(event);
    }

    private Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    private OrderItem toOrderItem(OrderItemRequest request) {
        return new OrderItem(request.productName(), request.price(), request.quantity());
    }

    private void publishAfterCommit(OrderChangedEvent event) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            orderEventPublisher.publish(event);
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                orderEventPublisher.publish(event);
            }
        });
    }
}
