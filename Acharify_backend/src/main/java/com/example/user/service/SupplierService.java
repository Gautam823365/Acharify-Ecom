package com.example.user.service;

import com.example.user.entity.Supplier;
import com.example.user.repo.SupplierRepository;


import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${app.supplier.default-password}")
    private String defaultPassword;
    public SupplierService(
            SupplierRepository supplierRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.supplierRepository = supplierRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Supplier saveSupplier(Supplier supplier) {

        // 🔐 SET DEFAULT PASSWORD ONLY ON CREATE
        if (supplier.getSupplierId() == null) {
            supplier.setPassword(passwordEncoder.encode(defaultPassword));
        }

        return supplierRepository.save(supplier);
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Optional<Supplier> getSupplierById(Long id) {
        return supplierRepository.findById(id);
    }

    public Optional<Supplier> updateSupplier(Long id, Supplier supplier) {
        return supplierRepository.findById(id).map(existing -> {
            existing.setSuppliersName(supplier.getSuppliersName());
            existing.setSupplierBusinessName(supplier.getSupplierBusinessName());
            existing.setSuppliersPhone(supplier.getSuppliersPhone());
            existing.setSuppliersEmail(supplier.getSuppliersEmail());
            existing.setSuppliersAddressId(supplier.getSuppliersAddressId());
            existing.setGstIdNo(supplier.getGstIdNo());
            existing.setCity(supplier.getCity());
            existing.setState(supplier.getState());
            existing.setPincode(supplier.getPincode());
            existing.setStatus(supplier.getStatus());
            return supplierRepository.save(existing);
        });
    }

    public void updatePassword(Long id, String rawPassword) {
        if (rawPassword == null || rawPassword.length() < 6) {
            throw new RuntimeException("Password must be at least 6 characters");
        }

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        supplier.setPassword(passwordEncoder.encode(rawPassword));
        supplierRepository.save(supplier);
    }

    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }
}