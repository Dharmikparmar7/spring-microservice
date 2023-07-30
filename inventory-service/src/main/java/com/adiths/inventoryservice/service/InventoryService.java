package com.adiths.inventoryservice.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adiths.inventoryservice.dto.InventoryRequest;
import com.adiths.inventoryservice.model.Inventory;
import com.adiths.inventoryservice.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public Map<String, Inventory> isInStock(List<String> productIdList) {
        return inventoryRepository.findByProductIdIn(productIdList).stream()
                .collect(Collectors.toMap(Inventory::getProductId, inventory -> inventory));
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
