package com.template.saga.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.mapper.OrderHistoryMapper;
import com.template.repository.OrderHistoryRepository;
import com.template.repository.OrderRepository;
import com.template.saga.event.PaymentCanceledEvent;
import com.template.saga.event.PaymentDoneEvent;
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
public class PaymentHandler {

    private static final String COMMAND_HEADER_NAME = "command";

    @Value("${spring.kafka.services.product.command.topic}")
    private String productCommandTopic;

    private final ObjectMapper objectMapper;
    private final OrderRepository orderRepository;
    private final OrderHistoryMapper orderHistoryMapper;
    private final OrderHistoryRepository orderHistoryRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void handleSuccess(PaymentDoneEvent event) {
        var order = orderRepository.findById(event.getOrderId());
        if (order.isPresent()) {
            var orderEntity = order.get();
            orderEntity.setStatus("ORDER_DONE");
            orderRepository.save(orderEntity);
            orderHistoryRepository.save(orderHistoryMapper.toOrderHistoryEntity(orderEntity));
        }
    }

    public void handleError(PaymentCanceledEvent event) {
        var order = orderRepository.findById(event.getOrderId());
        if (order.isPresent()) {
            var orderEntity = order.get();
            orderEntity.setStatus("ORDER_PAYMENT_FAILED");
            orderRepository.save(orderEntity);
            orderHistoryRepository.save(orderHistoryMapper.toOrderHistoryEntity(orderEntity));
        }

        try {
            kafkaTemplate.send(
                    MessageBuilder.withPayload(objectMapper.writeValueAsString(event))
                            .setHeader(KafkaHeaders.TOPIC, productCommandTopic)
                            .setHeader(COMMAND_HEADER_NAME, "PaymentCanceledEvent")
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
