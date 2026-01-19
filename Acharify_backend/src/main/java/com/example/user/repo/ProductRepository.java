package com.example.user.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.user.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStateName(String stateName);
    
    List<Product> findBySupplier_SupplierId(Long supplierId);
    
    boolean existsByProductsIdAndSupplier_SupplierId(Long productId, Long supplierId);

    void deleteByProductsIdAndSupplier_SupplierId(Long productId, Long supplierId);
    }
	