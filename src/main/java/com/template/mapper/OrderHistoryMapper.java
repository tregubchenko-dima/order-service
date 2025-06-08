package com.template.mapper;

import com.template.model.entity.OrderEntity;
import com.template.model.entity.OrderHistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderHistoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "orderId", source = "orderEntity.id")
    @Mapping(target = "name", source = "orderEntity.name")
    @Mapping(target = "price", source = "orderEntity.price")
    @Mapping(target = "status", source = "orderEntity.status")
    @Mapping(target = "date", source = "orderEntity.date")
    OrderHistoryEntity toOrderHistoryEntity(OrderEntity orderEntity);
}