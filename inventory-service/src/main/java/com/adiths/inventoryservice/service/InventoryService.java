package com.adiths.inventoryservice.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adiths.inventoryservice.dto.InventoryRequest;
import com.adiths.inventoryservice.model.Inventory;
import com.adiths.inventoryservice.repository.InventoryRepository;

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

    @Transactional()
    public String checkStock(List<InventoryRequest> orderInventories) {
        List<Inventory> inventories = inventoryRepository.findByProductIdIn(orderInventories.stream().map(inventory -> inventory.getProductId()).toList());
        Map<String, Inventory> inventoriesMap = inventories.stream().collect(Collectors.toMap(Inventory::getProductId, inventory -> inventory));

        boolean isInStock = true;

        for (int i = 0; i < inventories.size(); i++) {
            if (inventoriesMap.get(inventories.get(i).getProductId()) == null) {
                isInStock = false;
            } else if(inventoriesMap.get(inventories.get(i).getProductId()).getQuantity().compareTo(orderInventories.get(i).getQuantity()) < 0) {
                isInStock = false;
            }
            else{
                inventories.get(i).setQuantity(inventories.get(i).getQuantity() - orderInventories.get(i).getQuantity());
            }
        }

        if (!isInStock) {
            return "Not in Stock";
        }
        else
        {
            afterCommit(inventories);
            return "OK";
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void afterCommit(List<Inventory> inventories) {
        inventoryRepository.saveAll(inventories);
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
