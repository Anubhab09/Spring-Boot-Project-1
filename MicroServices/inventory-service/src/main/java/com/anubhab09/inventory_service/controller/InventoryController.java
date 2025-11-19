package com.anubhab09.inventory_service.controller;

import com.anubhab09.inventory_service.entity.InventoryItem;
import com.anubhab09.inventory_service.repository.InventoryRepository;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryRepository repo;

    @GetMapping
    private List<InventoryItem> listall(){
        return repo.findAll();
    }

    @GetMapping("/by-name/{productName}")
    private ResponseEntity<InventoryItem> getByName(@PathVariable String productName){
        return repo.findByProductName(productName)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    private ResponseEntity<InventoryItem> create(@RequestBody InventoryItem item){
        InventoryItem saved = repo.save(item);
        return ResponseEntity.created(URI.create("/inventory" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryItem> replace(@PathVariable Long id, @RequestBody InventoryItem body) {
        return repo.findById(id).map(existing -> {
            existing.setProductName(body.getProductName());
            existing.setQuantity(body.getQuantity());
            repo.save(existing);
            return ResponseEntity.ok(existing);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/quantity/{productName}")
    public ResponseEntity<InventoryItem> updateQty(@PathVariable String productName, @RequestParam int qty) {
        return repo.findByProductName(productName).map(item -> {
            item.setQuantity(qty);
            repo.save(item);
            return ResponseEntity.ok(item);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
