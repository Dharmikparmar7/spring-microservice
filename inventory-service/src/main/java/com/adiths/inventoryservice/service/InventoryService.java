package com.adiths.inventoryservice.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Transactional()
    public ResponseEntity<String> checkStock(List<InventoryRequest> orderInventories){

        for (InventoryRequest inventoryRequest : orderInventories) {
            Inventory inventory = inventoryRepository.findByProductId(inventoryRequest.getProductId());
            inventory.setQuantity(inventory.getQuantity() - inventoryRequest.getQuantity());
        }

        return new ResponseEntity<String>("OK", HttpStatus.OK);
    }

    public ResponseEntity<List<Inventory>> findAll() {
        return new ResponseEntity<List<Inventory>>(inventoryRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<HttpStatus> createInventory(InventoryRequest inventoryRequest) {

        Inventory inventory = Inventory.builder()
                .productId(inventoryRequest.getProductId())
                .quantity(inventoryRequest.getQuantity())
                .build();

        inventoryRepository.save(inventory);

        return new ResponseEntity<HttpStatus>(HttpStatus.CREATED);
    }
}
