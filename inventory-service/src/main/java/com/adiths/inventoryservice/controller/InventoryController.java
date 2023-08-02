package com.adiths.inventoryservice.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Inventory> isInStock(@RequestParam List<String> productIdList){
        return inventoryService.isInStock(productIdList);
    }

    @GetMapping("/all")
    public List<Inventory> getAllInventory(){
        return inventoryService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveInventory(@RequestBody InventoryRequest inventoryRequest){
        inventoryService.saveInventory(inventoryRequest);
    }

    @PostMapping("/check")
    @ResponseStatus(HttpStatus.CREATED)
    public String checkStock(@RequestBody List<InventoryRequest> inventoryRequest){
        return inventoryService.checkStock(inventoryRequest);
    }

    // @PutMapping("/{id}")
    // public ResponseEntity<Inventory> updateInventory(@PathVariable Long id, @RequestBody Inventory updatedInventory) {
    //     if (!id.equals(updatedInventory.getId())) {
    //         return ResponseEntity.badRequest().build();
    //     }

    //     Inventory updatedItem = inventoryService.updateInventory(updatedInventory);
    //     return ResponseEntity.ok(updatedItem);
    // }
}
