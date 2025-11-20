package com.anubhab09.inventory_service.service;

import com.anubhab09.inventory_service.entity.InventoryItem;
import com.anubhab09.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository repo;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(InventoryService.class);

    @Cacheable(value = "inventoryByName", key = "#productName")
    public Optional<InventoryItem> getByProductName(String productName) {
        return repo.findByProductName(productName);
    }


    @CachePut(value = "inventoryByName", key = "#productName")
    @CacheEvict(value = "inventoryAll", allEntries = true)
    public boolean reserveItem(String productName, int qtyToReserve){
        return repo.findByProductName(productName)
                .map(inventoryItem -> {
                    if(inventoryItem.getQuantity() >= qtyToReserve){
                        inventoryItem.setQuantity(inventoryItem.getQuantity() - qtyToReserve);
                        repo.save(inventoryItem);
                        log.info("Reserve {} of {} (remaining={})", qtyToReserve, productName, inventoryItem.getQuantity());
                        return true;
                    } else{
                        log.warn("Out of stock for product {}: requested={}, available={}", productName, qtyToReserve, inventoryItem.getQuantity());
                        return false;
                    }
                })
                .orElseGet(() ->{
                    log.warn("No inventory found for product {}", productName);
                    return false;
                });
    }


}
