package com.template.saga;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.saga.event.PaymentCanceledEvent;
import com.template.saga.event.PaymentDoneEvent;
import com.template.saga.event.ProductCanceledEvent;
import com.template.saga.event.ProductTookEvent;
import com.template.saga.handler.PaymentHandler;
import com.template.saga.handler.ProductHandler;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Saga для управления заказом
 */
@Component
@RequiredArgsConstructor
public class OrderSaga {

    private final ObjectMapper objectMapper;
    private final PaymentHandler paymentHandler;
    private final ProductHandler productHandler;

    @KafkaListener(topics = "${spring.kafka.command.topic}")
    public void consume(ConsumerRecord<String, String> event) {
        var header = event.headers().lastHeader("command");
        if (header != null) {
            if (String.valueOf(header.value()).equals("ProductTookEvent")) {
                try {
                    var productTookEvent = objectMapper.readValue(event.value(), ProductTookEvent.class);
                    productHandler.handleSuccess(productTookEvent);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else if (String.valueOf(header.value()).equals("ProductCancelledEvent")) {
                try {
                    var productCanceledEvent = objectMapper.readValue(event.value(), ProductCanceledEvent.class);
                    productHandler.handleError(productCanceledEvent);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else if (String.valueOf(header.value()).equals("PaymentDoneEvent")) {
                try {
                    var paymentDoneEvent = objectMapper.readValue(event.value(), PaymentDoneEvent.class);
                    paymentHandler.handleSuccess(paymentDoneEvent);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else if (String.valueOf(header.value()).equals("PaymentCancelledEvent")) {
                try {
                    var paymentCanceledEvent = objectMapper.readValue(event.value(), PaymentCanceledEvent.class);
                    paymentHandler.handleError(paymentCanceledEvent);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
