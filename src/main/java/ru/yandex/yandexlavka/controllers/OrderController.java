package ru.yandex.yandexlavka.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.UUID;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.yandexlavka.dto.OrderDTO;
import ru.yandex.yandexlavka.dto.request.CompleteOrderRequestDTO;
import ru.yandex.yandexlavka.dto.request.CreateOrderRequestDTO;
import ru.yandex.yandexlavka.dto.response.OrderAssignResponseDTO;
import ru.yandex.yandexlavka.services.OrderService;

import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequestMapping("orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public List<OrderDTO> createOrders(@RequestBody @Valid CreateOrderRequestDTO request) {
        return orderService.createOrders(request.getOrders());
    }

    @GetMapping
    public List<OrderDTO> ordersList(@RequestParam(required = false, defaultValue = "1") @Positive Integer limit,
                                     @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer offset) {
        return orderService.ordersList(limit, offset);
    }

    @GetMapping(path = "{order_id}")
    public OrderDTO orderInfo(@PathVariable(name = "order_id") @Positive Long id) {
        return orderService.orderInfo(id);
    }

    @PostMapping(path = "complete")
    @Cacheable(cacheNames = "idempotent-responses", key = "#idempotencyKey", condition = "#idempotencyKey != null")
    public List<OrderDTO> completeOrders(@RequestBody @Valid CompleteOrderRequestDTO request,
                                         @RequestHeader(name = "Idempotency-Key", required = false) @UUID(letterCase = UUID.LetterCase.INSENSITIVE) String idempotencyKey) {
        return orderService.completeOrders(request.getCompleteInfo());
    }

    @PostMapping(path = "assign")
    public ResponseEntity<OrderAssignResponseDTO> assignOrders(@RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now()}") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PastOrPresent LocalDate date) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.assignOrders(date));
    }
}
