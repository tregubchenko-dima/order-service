package com.template.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.mapper.OrderHistoryMapper;
import com.template.mapper.OrderMapper;
import com.template.repository.OrderHistoryRepository;
import com.template.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapi.template.model.OrderCreateRequest;
import org.openapi.template.model.OrderCreateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Сервис для работы с шаблонным классом
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private static final String COMMAND_HEADER_NAME = "command";

    private final OrderMapper orderMapper;
    private final ObjectMapper objectMapper;
    private final OrderRepository orderRepository;
    private final OrderHistoryMapper orderHistoryMapper;
    private final OrderHistoryRepository orderHistoryRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.services.product.command.topic}")
    private String productServiceCommandTopic;

    @Transactional
    public OrderCreateResponse createOrder(OrderCreateRequest request) {
        var orderEntity = orderMapper.toOrderEntity(request);

        orderRepository.save(orderEntity);
        orderHistoryRepository.save(orderHistoryMapper.toOrderHistoryEntity(orderEntity));

        try {
            kafkaTemplate.send(
                    MessageBuilder.withPayload(objectMapper.writeValueAsString(orderMapper.toOrderBookedEvent(orderEntity)))
                            .setHeader(KafkaHeaders.TOPIC, productServiceCommandTopic)
                            .setHeader(COMMAND_HEADER_NAME, "OrderBookedEvent")
                            .build()
            );
        } catch (Exception e) {
            log.info("Ошибки при сериализации");
            throw new RuntimeException();
        }

        return orderMapper.toOrderCreateResponse(orderEntity);
    }

}