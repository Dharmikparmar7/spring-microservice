package com.adiths.orderservice.orderservice.service;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
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
        List<Items> items = orderRequest.getItems().stream().map(this::mapToEntity).toList();

        List<String> productIdList = items.stream().map(this::mapToInventory).toList();

        Map<String, Inventory> inventoriesMap = webClient
                .get()
                .uri(UriBuilder -> UriBuilder.path("/inventory").queryParam("productIdList", productIdList).build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Inventory>>() {
                })
                .block();

        boolean isInStock = true;
        // productIdList.removeAll(productIdList);

        for (int i = 0; i < items.size(); i++) {
            if (inventoriesMap.get(items.get(i).getProductId()) == null) {
                isInStock = false;
                // productIdList.add(items.get(i).getProductId().toString());
            } else if(inventoriesMap.get(items.get(i).getProductId()).getQuantity().compareTo(items.get(i).getQuantity()) < 0) {
                isInStock = false;
            }
        }

        if (!isInStock) {
            log.info("Order is not placed !");
            // productIdList.forEach(id -> log.info(id));
            log.info("Some products are not in stock");
            return Order.builder().build();
        }

        Order order = Order.builder()
                .orderNumber(orderRequest.getOrderNumber())
                .items(items)
                .build();

        items.forEach(item -> item.setOrder(order));

        orderRepository.save(order);
        log.info("Order Placed");
        return order;
    }

    private Items mapToEntity(ItemsDto itemsDto) {
        Items item = new Items();

        item.setProductId(itemsDto.getProductId());
        item.setPrice(itemsDto.getPrice());
        item.setQuantity(itemsDto.getQuantity());

        return item;
    }

    private String mapToInventory(Items item) {
        return new String(item.getProductId());
    }
}