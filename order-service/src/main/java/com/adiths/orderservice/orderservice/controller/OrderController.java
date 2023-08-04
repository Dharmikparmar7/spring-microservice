package com.adiths.orderservice.orderservice.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.adiths.orderservice.orderservice.dto.OrderRequest;
import com.adiths.orderservice.orderservice.model.Order;
import com.adiths.orderservice.orderservice.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Order>> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping("{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Optional<Order>> getOrderById(@PathVariable Long orderID){
        return orderService.getOrderById(orderID);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest orderRequest){
        return orderService.createOrder(orderRequest);
    }
}
