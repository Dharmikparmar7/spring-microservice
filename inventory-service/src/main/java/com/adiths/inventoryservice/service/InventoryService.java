package com.adiths.inventoryservice.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adiths.inventoryservice.dto.InventoryRequest;
import com.adiths.inventoryservice.exception.OutOfStockException;
import com.adiths.inventoryservice.model.Inventory;
import com.adiths.inventoryservice.repository.InventoryRepository;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public Map<String, Inventory> isInStock(List<String> productIdList) {
        return inventoryRepository.findByProductIdIn(productIdList).stream()
                .collect(Collectors.toMap(Inventory::getProductId, inventory -> inventory));
    }

    @Transactional(rollbackFor = ConstraintViolationException.class)
    public String checkStock(List<InventoryRequest> orderInventories){
        log.info("inventory service called");

        List<Inventory> inventories = inventoryRepository.findByProductIdIn(orderInventories.stream().map(inventory -> inventory.getProductId()).toList());

        for (int i = 0; i < inventories.size(); i++) {
            // Inventory inventory = inventories.get(i);
            // int orderedQuantity = orderInventories.get(i).getQuantity();
            // int availableQuantity = inventory.getQuantity();

            // if (availableQuantity < orderedQuantity) {
            //     throw new ConstraintViolationException("Out of Stock", null);
            // }

            // inventory.setQuantity(availableQuantity - orderedQuantity);
            inventories.get(i).setQuantity(inventories.get(i).getQuantity() - orderInventories.get(i).getQuantity());
        }
        
        inventoryRepository.saveAll(inventories);
        log.info("OK");

        return "OK";
    }

    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }

    public void saveInventory(InventoryRequest inventoryRequest) {

        Inventory inventory = Inventory.builder()
                .productId(inventoryRequest.getProductId())
                .quantity(inventoryRequest.getQuantity())
                .build();

        inventoryRepository.save(inventory);
    }

    // public Inventory updateInventory(Inventory updatedInventory) throws NotFoundException {
    //     Inventory existingInventory = inventoryRepository.findByProductIdIn();

    //     return inventoryRepository.save(existingInventory);
    // }
}
