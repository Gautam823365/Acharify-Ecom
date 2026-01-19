package com.example.user.service;


import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.user.dto.CheckoutRequest;
import com.example.user.entity.Cart;
import com.example.user.entity.CartItem;
import com.example.user.entity.CheckoutResponse;
import com.example.user.entity.Product;
import com.example.user.entity.PurchaseOrder;
import com.example.user.entity.User;
import com.example.user.repo.ProductRepository;
import com.example.user.repo.PurchaseOrderRepository;
import com.example.user.repo.UserRepository;
@Service
public class CheckoutService {

    private final PurchaseOrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CheckoutService(PurchaseOrderRepository orderRepository,
                           UserRepository userRepository,
                           ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public CheckoutResponse placeOrder(String username, Cart cart, CheckoutRequest request) {

        // 1️⃣ Fetch logged-in user
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
        // 3️⃣ Get first product from cart
        CartItem cartItem = cart.getItems().get(0);

     // 4️⃣ Fetch product using productId
     Product product = productRepository.findById(cartItem.getProductId())
             .orElseThrow(() -> new RuntimeException("Product not found"));

        // 4️⃣ Get supplier ID from product
        Long supplierId = product.getSupplier().getSupplierId();

        // 5️⃣ Create Purchase Order
        PurchaseOrder order = new PurchaseOrder();
        order.setPouserid(user.getId());
        order.setPosupplierid(supplierId);
        order.setPoorderdate(LocalDateTime.now());
        order.setPoexpecteddeliveryDate(request.getDeliveryDate());
        order.setPodeliverystatus("PLACED");

        // 6️⃣ Save order
        PurchaseOrder savedOrder = orderRepository.save(order);

        // 7️⃣ Return response
        return new CheckoutResponse(
                savedOrder.getPoid(),
                "SUCCESS",
                "Order placed successfully"
        );
    }
}
