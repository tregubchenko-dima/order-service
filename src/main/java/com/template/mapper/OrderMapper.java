package com.template.mapper;

import com.template.model.entity.OrderEntity;
import com.template.saga.event.OrderBookedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapi.template.model.OrderCreateRequest;
import org.openapi.template.model.OrderCreateResponse;

import java.time.LocalDate;

@Mapper(componentModel = "spring", imports = {LocalDate.class})
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "price", source = "request.price")
    @Mapping(target = "status", constant = "NEW")
    @Mapping(target = "date", expression = "java(LocalDate.now())")
    OrderEntity toOrderEntity(OrderCreateRequest request);

    @Mapping(target = "id", source = "orderEntity.id")
    @Mapping(target = "name", source = "orderEntity.name")
    @Mapping(target = "status", source = "orderEntity.status")
    @Mapping(target = "date", source = "orderEntity.date")
    OrderCreateResponse toOrderCreateResponse(OrderEntity orderEntity);

    @Mapping(target = "id", source = "orderEntity.id")
    @Mapping(target = "name", source = "orderEntity.name")
    @Mapping(target = "price", source = "orderEntity.price")
    @Mapping(target = "status", source = "orderEntity.status")
    @Mapping(target = "date", source = "orderEntity.date")
    OrderBookedEvent toOrderBookedEvent(OrderEntity orderEntity);
}