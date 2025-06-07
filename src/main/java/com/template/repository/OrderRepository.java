package com.template.repository;


import com.template.model.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Репозиторий для работы с {@link OrderEntity}
 */
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
}
