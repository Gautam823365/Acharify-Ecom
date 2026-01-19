package com.example.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.user.entity.PurchaseOrder;

import java.util.List;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
  
}
