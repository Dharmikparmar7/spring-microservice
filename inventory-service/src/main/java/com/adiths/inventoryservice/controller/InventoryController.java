package com.adiths.inventoryservice.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.adiths.inventoryservice.dto.InventoryRequest;
import com.adiths.inventoryservice.model.Inventory;
import com.adiths.inventoryservice.service.InventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    
    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventory(){
        return inventoryService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createInventory(@RequestBody InventoryRequest inventoryRequest){
        inventoryService.createInventory(inventoryRequest);
    }

    @PostMapping("/check")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> checkStock(@RequestBody List<InventoryRequest> inventoryRequest){
        return inventoryService.checkStock(inventoryRequest);
    }
}