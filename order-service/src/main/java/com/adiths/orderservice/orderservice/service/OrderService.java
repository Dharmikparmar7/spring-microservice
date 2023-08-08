package com.adiths.orderservice.orderservice.service;

import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.adiths.orderservice.orderservice.dto.Inventory;
import com.adiths.orderservice.orderservice.dto.ItemsDto;
import com.adiths.orderservice.orderservice.dto.OrderRequest;
import com.adiths.orderservice.orderservice.model.Items;
import com.adiths.orderservice.orderservice.model.Order;
import com.adiths.orderservice.orderservice.repository.OrderRepository;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    private final WebClient.Builder webClientBuilder;

    public ResponseEntity<List<Order>> getAllOrders() {
        return new ResponseEntity<>(orderRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<Optional<Order>> getOrderById(Long Id) {
        return new ResponseEntity<>(orderRepository.findById(Id), HttpStatus.FOUND);
    }

    public ResponseEntity<String> createOrder(OrderRequest orderRequest) {
        List<Items> items = orderRequest.getItems().stream().map(this::mapToItem).toList();

        List<Inventory> inventories = orderRequest.getItems().stream().map(this::mapToInventory).toList();

        ResponseEntity<String> isInStock = webClientBuilder.build().post()
                .uri("http://inventory-service/api/inventory/check")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inventories), List.class)
                .retrieve()
                .toEntity(String.class)
                .block();

        log.info("return body ::: ");
        log.info(isInStock.getBody());
        log.info("return ::: ");
        log.info(isInStock.getStatusCode().toString());

        if (isInStock != null && isInStock.getStatusCode().equals(HttpStatus.OK)) {
            Order order = Order.builder()
                    .orderNumber(orderRequest.getOrderNumber())
                    .items(items)
                    .build();

            items.forEach(item -> item.setOrder(order));

            orderRepository.save(order);
            return new ResponseEntity<>("Order Placed", HttpStatus.CREATED);
        }

        return new ResponseEntity<>("Order is not placed", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Inventory mapToInventory(ItemsDto itemsDto) {
        return Inventory.builder().productId(itemsDto.getProductId()).quantity(itemsDto.getQuantity()).build();
    }

    private Items mapToItem(ItemsDto itemsDto) {
        Items items = new Items();

        items.setPrice(itemsDto.getPrice());
        items.setProductId(itemsDto.getProductId());
        items.setQuantity(itemsDto.getQuantity());

        return items;
    }
}