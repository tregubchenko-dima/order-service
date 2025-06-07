package com.template.controller;


import com.template.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.openapi.template.OrdersApi;
import org.openapi.template.model.OrderCreateRequest;
import org.openapi.template.model.OrderCreateResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Шаблонный контроллер
 */
@RestController
@RequiredArgsConstructor
public class OrderController implements OrdersApi {

    private final OrderService orderService;

    @Override
    public ResponseEntity<OrderCreateResponse> createOrder(OrderCreateRequest orderCreateRequest) {
        return ResponseEntity.ok(orderService.createOrder(orderCreateRequest));
    }
}
