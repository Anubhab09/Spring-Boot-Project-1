package com.anubhab09.inventory_service.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "inventory_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String productName;

    @Column(nullable = false)
    private Integer quantity;
}
