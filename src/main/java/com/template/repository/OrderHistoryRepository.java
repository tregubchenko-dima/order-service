package com.template.repository;

import com.template.model.entity.OrderEntity;
import com.template.model.entity.OrderHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


/**
 * Репозиторий для работы с {@link OrderHistoryEntity}
 */
public interface OrderHistoryRepository extends JpaRepository<OrderHistoryEntity, UUID> {
}
