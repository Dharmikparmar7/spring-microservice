package com.adiths.inventoryservice.service;

import java.util.List;

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
    public boolean isInStock(String productId){
        return inventoryRepository.findByProductId(productId).isPresent();
    }

    public List<Inventory> findAll(){
        return inventoryRepository.findAll();
    }

    public void saveInventory(InventoryRequest inventoryRequest){
        Inventory inventory = Inventory.builder()
            .productId(inventoryRequest.getProductId())
            .quantity(inventoryRequest.getQuantity())
            .build();

        inventoryRepository.save(inventory);
    }
}
