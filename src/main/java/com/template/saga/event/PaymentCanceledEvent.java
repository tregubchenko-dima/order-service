package com.template.saga.event;

import lombok.Data;

import java.util.UUID;

@Data
public class PaymentCanceledEvent {

    private UUID orderId;
    private String name;
}
