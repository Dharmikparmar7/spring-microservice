package com.adiths.orderservice.orderservice.controller;

import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adiths.orderservice.orderservice.dto.OrderRequest;
import com.adiths.orderservice.orderservice.model.Order;
import com.adiths.orderservice.orderservice.service.OrderService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping("{orderId}")
    public ResponseEntity<Optional<Order>> getOrderById(@PathVariable Long orderId){
        return orderService.getOrderById(orderId);
    }

    @PostMapping
    @CircuitBreaker(name = "order", fallbackMethod = "myMethod")
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest orderRequest){
        log.info("oc called");
        return orderService.createOrder(orderRequest);
    }

    public ResponseEntity<String> myMethod(OrderRequest orderRequest, RuntimeException runtimeException){
        return new ResponseEntity<>("Some error occurred !", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
