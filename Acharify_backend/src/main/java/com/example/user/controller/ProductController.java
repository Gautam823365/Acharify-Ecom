package com.example.user.controller;

import com.example.user.config.UserPrincipal;
import com.example.user.entity.Product;
import com.example.user.entity.Supplier;
import com.example.user.repo.SupplierRepository;
import com.example.user.service.CustomUserDetailsService;
import com.example.user.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // ✅ CREATE product (Supplier only)
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Product> createProduct(
            @RequestParam("productsName") String productsName,
            @RequestParam(value = "productsDescription", required = false) String productsDescription,
            @RequestParam("productsCategoryId") Long productsCategoryId,
            @RequestParam("productsUnitPrice") Double productsUnitPrice,
            @RequestParam("productsQuantity") Integer productsQuantity,
            @RequestParam("productsThreshold") Integer productsThreshold,
            @RequestParam("stateName") String stateName,
            @RequestParam("image") MultipartFile image,
            Authentication authentication
    ) {
        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        Long supplierId = principal.getSupplierId();
        if (supplierId == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Product saved = productService.saveProductWithImage(
                productsName,
                productsDescription,
                productsCategoryId,
                productsUnitPrice,
                productsQuantity,
                productsThreshold,
                stateName,
                image,
                supplierId
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }


    // ✅ UPDATE product
//    @PutMapping("/{id}")
//    public ResponseEntity<Product> updateProduct(
//            @PathVariable Long id,
//            @RequestBody Product payload,
//            Authentication authentication) {
//        Product existing = productService.getProductById(id);
//        if (existing == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        // 🔐 OPTIONAL: enforce ownership
//        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
//        if (principal.getSupplierId() != null &&
//            !existing.getSupplier().getSupplierId().equals(principal.getSupplierId())) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
//
//        existing.setProductsName(payload.getProductsName());
//        existing.setProductsDescription(payload.getProductsDescription());
//        existing.setProductsUnitPrice(payload.getProductsUnitPrice());
//        existing.setProductsQuantity(payload.getProductsQuantity());
//        existing.setProductsThreshold(payload.getProductsThreshold());
//        existing.setProductsImage(payload.getProductsImage());
//        existing.setStateName(payload.getStateName());
//
//        return ResponseEntity.ok(productService.updateProduct(existing));
//    }

    // ✅ GET all or by state
    @GetMapping
    public ResponseEntity<List<Product>> getProducts(
            @RequestParam(required = false) String stateName) {

        return ResponseEntity.ok(
                stateName != null
                        ? productService.getProductsByState(stateName)
                        : productService.getAllProducts()
        );
    }

    // ✅ GET by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return product != null
                ? ResponseEntity.ok(product)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id,
            Authentication authentication) {

        if (authentication == null ||
            !(authentication.getPrincipal() instanceof UserPrincipal)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        boolean deleted =
                productService.deleteProduct(id, principal);

        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }



    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<Product>> getProductsBySupplier(
            @PathVariable Long supplierId,
            Authentication authentication) {

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        // SUPPLIER → only own products
        if (principal.getSupplierId() != null &&
            !principal.getSupplierId().equals(supplierId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(
                productService.getProductsBySupplier(supplierId)
        );
    }

}
