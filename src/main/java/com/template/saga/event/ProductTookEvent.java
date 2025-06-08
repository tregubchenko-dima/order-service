package com.template.saga.event;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductTookEvent {

    private UUID orderId;
    private String name;
    private Integer price;
    private Integer count;
}
