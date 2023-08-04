package com.adiths.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import com.adiths.inventoryservice.model.Inventory;

import jakarta.persistence.LockModeType;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Inventory findByProductId(String productId);
}
