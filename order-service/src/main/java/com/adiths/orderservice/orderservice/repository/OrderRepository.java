package com.adiths.orderservice.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adiths.orderservice.orderservice.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{
    
}
