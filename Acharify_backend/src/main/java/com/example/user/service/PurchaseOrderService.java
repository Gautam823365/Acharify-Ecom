package com.example.user.service;

import com.example.user.dto.OrderDTO;
import com.example.user.dto.OrderItemDTO;
import com.example.user.entity.PurchaseOrder;
import com.example.user.entity.User;
import com.example.user.entity.Supplier;
import com.example.user.entity.Product;
import com.example.user.repo.ProductRepository;
import com.example.user.repo.PurchaseOrderRepository;
import com.example.user.repo.SupplierRepository;
import com.example.user.repo.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class PurchaseOrderService {

    private final PurchaseOrderRepository orderRepository;
    private final UserRepository userRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;

    public PurchaseOrderService(PurchaseOrderRepository orderRepository,
                                UserRepository userRepository,
                                SupplierRepository supplierRepository,
                                ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.supplierRepository = supplierRepository;
        this.productRepository = productRepository;
    }

    public List<OrderDTO> getAllOrdersDTO() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO getOrderDTOById(Long id) {
        PurchaseOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return convertToDTO(order);
    }

    private OrderDTO convertToDTO(PurchaseOrder order) {
        // ✅ get user fullName instead of username
        String userName = userRepository.findById(order.getPouserid())
                .map(User::getFullName)
                .orElse("Unknown User");

        // ✅ use suppliersName instead of getName()
        String supplierName = supplierRepository.findById(order.getPosupplierid())
                .map(Supplier::getSuppliersName)
                .orElse("Unknown Supplier");

        // ✅ use productsName instead of getName()
        List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(item -> {
                    String productName = productRepository.findById(item.getProductId())
                            .map(Product::getProductsName)
                            .orElse("Unknown Product");
                    return new OrderItemDTO(item.getProductId(), productName, item.getQuantity(), item.getPrice());
                })
                .collect(Collectors.toList());

        return new OrderDTO(
                order.getPouserid(),
                userName,
                supplierName,
                order.getPoorderdate(),
                order.getPoexpecteddeliveryDate(),
                order.getPodeliverystatus(),
                itemDTOs
        );
    }
}
