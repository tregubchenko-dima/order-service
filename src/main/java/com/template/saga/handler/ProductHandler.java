package com.template.saga.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.mapper.OrderHistoryMapper;
import com.template.repository.OrderHistoryRepository;
import com.template.repository.OrderRepository;
import com.template.saga.event.ProductCanceledEvent;
import com.template.saga.event.ProductTookEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductHandler {

    private static final String COMMAND_HEADER_NAME = "command";

    @Value("${spring.kafka.services.payment.command.topic}")
    private String paymentCommandTopic;

    private final ObjectMapper objectMapper;
    private final OrderRepository orderRepository;
    private final OrderHistoryMapper orderHistoryMapper;
    private final OrderHistoryRepository orderHistoryRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void handleSuccess(ProductTookEvent event) {
        var order = orderRepository.findById(event.getOrderId());
        if (order.isPresent()) {
            var orderEntity = order.get();
            orderEntity.setStatus("ORDER_EQUIPPED");
            orderRepository.save(orderEntity);
            orderHistoryRepository.save(orderHistoryMapper.toOrderHistoryEntity(orderEntity));
        }

        try {
            kafkaTemplate.send(
                    MessageBuilder.withPayload(objectMapper.writeValueAsString(event))
                            .setHeader(KafkaHeaders.TOPIC, paymentCommandTopic)
                            .setHeader(COMMAND_HEADER_NAME, "ProductTookEvent")
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void handleError(ProductCanceledEvent event) {
        var order = orderRepository.findById(event.getOrderId());
        if (order.isPresent()) {
            var orderEntity = order.get();
            orderEntity.setStatus("ORDER_CANCELLED");
            orderRepository.save(orderEntity);
            orderHistoryRepository.save(orderHistoryMapper.toOrderHistoryEntity(orderEntity));
        }
    }
}
