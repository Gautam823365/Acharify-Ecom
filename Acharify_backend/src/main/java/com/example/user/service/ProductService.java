package com.example.user.service;

import com.example.user.config.UserPrincipal;
import com.example.user.entity.Product;
import com.example.user.entity.Supplier;
import com.example.user.repo.ProductRepository;
import com.example.user.repo.SupplierRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SupplierRepository supplierRepository;

//    // Save multiple products at once
//    public List<Product> saveAllProducts(List<Product> products) {
//        for (Product product : products) {
//            if (product.getSupplier() == null || product.getSupplier().getSupplierId() == null) {
//                throw new IllegalArgumentException("Product must contain a valid supplier with supplierId");
//            }
//
//            Long supplierId = product.getSupplier().getSupplierId();
//
//            // Fetch the existing supplier from DB to get a managed entity
//            Supplier managedSupplier = supplierRepository.findById(supplierId)
//                    .orElseThrow(() -> new RuntimeException("Supplier not found with ID: " + supplierId));
//
//            product.setSupplier(managedSupplier);
//        }
//        return productRepository.saveAll(products);
//    }
    public Product saveProduct(Product product, Long supplierId) {

        Supplier supplier = supplierRepository.findById(supplierId)
            .orElseThrow(() -> new RuntimeException("Supplier not found"));

        product.setSupplier(supplier);
        
        System.out.println("ID before save = " + product.getProductsId());
        System.out.println("Version before save = " + product.getVersion());

        return productRepository.save(product);
    }

    public Product saveProductWithImage(
            String name,
            String description,
            Long categoryId,
            Double price,
            Integer quantity,
            Integer threshold,
            String stateName,
            MultipartFile image,
            Long supplierId
    ) {

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        String imageUrl = storeImage(image);

        Product product = new Product();
        product.setProductsName(name);
        product.setProductsDescription(description);
//        product.setProductsCategoryId(categoryId);
        product.setProductsUnitPrice(price);
        product.setProductsQuantity(quantity);
        product.setProductsThreshold(threshold);
        product.setStateName(stateName);
        product.setProductsImage(imageUrl); // ✅ URL ONLY
        product.setSupplier(supplier);

        return productRepository.save(product);
    }


    // Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    } 
    
//    public Product saveProduct(Product product) {
//
//        if (product.getProductsId() != null) {
//           
//            Product existing = productRepository.findById(product.getProductsId())
//                    .orElseThrow(() -> new RuntimeException("Product not found"));
//
//            existing.setProductsName(product.getProductsName());
//            existing.setProductsDescription(product.getProductsDescription());
//            existing.setProductsUnitPrice(product.getProductsUnitPrice());
//            existing.setProductsQuantity(product.getProductsQuantity());
//            existing.setProductsImage(product.getProductsImage());
//            existing.setStateName(product.getStateName());
//            existing.setProductsThreshold(product.getProductsThreshold());
//
//            // Set supplier if needed
//            if (product.getSupplier() != null) {
//                existing.setSupplier(product.getSupplier());
//            }
//
//            return productRepository.save(existing);
//        } else {
//            // Creating new product
////            product.setProductsId(null); // ensure Hibernate treats it as new
////            product.setVersion(null);    // version will be handled by Hibernate
//
//            // Make sure supplier is set
//            if (product.getSupplier() == null) {
//                throw new RuntimeException("Supplier must be set for new product");
//            }
//
//            return productRepository.save(product);
//        }
//    }

    // Get product by ID
    public Product getProductById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.orElse(null);
    }

    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    // Bulk update
    public List<Product> updateProducts(List<Product> productsToUpdate) {
        List<Product> updated = new ArrayList<>();
        for (Product p : productsToUpdate) {
            updated.add(updateProduct(p));
        }
        return updated;
    }

    
    public List<Product> getProductsBySupplier(Long supplierId) {
        return productRepository.findBySupplier_SupplierId(supplierId);
    }

    public boolean deleteProduct(Long productId, UserPrincipal principal) {

        // ADMIN → delete directly
        if (principal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {

            if (!productRepository.existsById(productId)) {
                return false;
            }

            productRepository.deleteById(productId);
            return true;
        }

        // SUPPLIER → delete only own product
        Long supplierId = principal.getSupplierId();
        if (supplierId == null) {
            return false;
        }

        boolean owned =
                productRepository.existsByProductsIdAndSupplier_SupplierId(
                        productId, supplierId);

        if (!owned) {
            return false;
        }

        productRepository.deleteByProductsIdAndSupplier_SupplierId(
                productId, supplierId);

        return true;
    }
 

    private String storeImage(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path uploadDir = Paths.get("uploads");

            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path filePath = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "http://localhost:8095/uploads/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }


    // Find products by stateName
    public List<Product> getProductsByState(String stateName) {
        return productRepository.findByStateName(stateName);
    }
}
