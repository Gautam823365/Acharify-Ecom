package com.example.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.user.entity.PurchaseOrderItem;

import java.util.List;

public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItem, Long> {
}

