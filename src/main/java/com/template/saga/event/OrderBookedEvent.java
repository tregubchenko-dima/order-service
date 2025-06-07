package com.template.saga.event;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class OrderBookedEvent {

    private UUID id;
    private String name;
    private Integer price;
    private String status;
    private LocalDate date;
}
