package com.adiths.orderservice.orderservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.adiths.orderservice.orderservice.dto.ItemsDto;
import com.adiths.orderservice.orderservice.dto.OrderRequest;
import com.adiths.orderservice.orderservice.model.Items;
import com.adiths.orderservice.orderservice.model.Order;
import com.adiths.orderservice.orderservice.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long Id){
        return orderRepository.findById(Id);
    }

    @Transactional
    public Order createOrder(OrderRequest orderRequest){

        List<Items> items = orderRequest.getItems().stream().map(this::mapToEntity).toList();

        Order order = Order.builder()
                        .orderNumber(orderRequest.getOrderNumber())
                        .items(items)
                        .build();
        
        items.forEach(item -> item.setOrder(order));

        orderRepository.save(order);
        return order;
    }

    private Items mapToEntity(ItemsDto itemsDto){
        Items item = new Items();

        item.setProductId(itemsDto.getProductId());
        item.setPrice(itemsDto.getPrice());
        item.setQuantity(itemsDto.getQuantity());

        return item;
    }
}