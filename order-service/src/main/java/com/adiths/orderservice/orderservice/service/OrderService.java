package com.adiths.orderservice.orderservice.service;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
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

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import org.springframework.web.util.UriBuilder;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    private final WebClient webClient;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long Id) {
        return orderRepository.findById(Id);
    }

    @Transactional
    public Order createOrder(OrderRequest orderRequest) {
        log.info("orderService");
        
        // Map<String, Inventory> inventoriesMap = webClient
        //         .get()
        //         .uri(UriBuilder -> UriBuilder.path("/inventory").queryParam("productIdList", productIdList).build())
        //         .retrieve()
        //         .bodyToMono(new ParameterizedTypeReference<Map<String, Inventory>>() {
        //         })
        //         .block();

        List<Items> items = orderRequest.getItems().stream().map(this::mapToDto).toList();

        List<Inventory> inventories = orderRequest.getItems().stream().map(this::mapToEntity).toList();
        
        try {
            ResponseEntity<String> isInStock = webClient.post()
                .uri("http://localhost:8081/api/inventory/check")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inventories), List.class)
                .retrieve()
                .toEntity(String.class)
                .block();
                
            log.info("return :::" + isInStock.getBody());

            if(isInStock.getBody().equals("OK"))
            {
                Order order = Order.builder()
                        .orderNumber(orderRequest.getOrderNumber())
                        .items(items)
                        .build();
        
                items.forEach(item -> item.setOrder(order));
        
                orderRepository.save(order);
                log.info("Order Placed");
                return order;
            }
        } catch (Exception e) {
            // Handle the exception (e.g., log the error, throw a custom exception, etc.)
            // For demonstration purposes, we'll simply print the error message here.
            System.err.println("RETURN ::: Error while performing the request: " + e.getMessage());
            return null;
        }
        return null;
    }

    private Inventory mapToEntity(ItemsDto itemsDto) {
        Inventory inventory = new Inventory();

        inventory.setProductId(itemsDto.getProductId());
        inventory.setQuantity(itemsDto.getQuantity());

        return inventory;
    }

    private String mapToInventory(Items item) {
        return new String(item.getProductId());
    }

    private Items mapToDto(ItemsDto itemsDto){
        Items items = new Items();

        items.setPrice(itemsDto.getPrice());
        items.setProductId(itemsDto.getProductId());
        items.setQuantity(itemsDto.getQuantity());

        return items;
    }
}