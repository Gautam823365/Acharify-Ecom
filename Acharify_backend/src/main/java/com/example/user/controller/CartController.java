package com.example.user.controller;


import com.example.user.entity.Cart;
import com.example.user.entity.CartItem;
import com.example.user.repo.CartRepository;
import com.example.user.service.CheckoutService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartRepository cartRepository;
    public CartController(CartRepository cartRepository,
                          CheckoutService checkoutService) {
        this.cartRepository = cartRepository;
    }

    @GetMapping
    public ResponseEntity<Cart> getCart(@AuthenticationPrincipal UserDetails user) {
        Cart cart = cartRepository.findByUsername(user.getUsername())
                .orElseGet(() -> {
                    Cart c = new Cart();
                    c.setUsername(user.getUsername());
                    return cartRepository.save(c);
                });
        return ResponseEntity.ok(cart);
    }

    // ✅ Same product → increase quantity
    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam Long productId) {

        Cart cart = cartRepository.findByUsername(user.getUsername())
                .orElseGet(() -> {
                    Cart c = new Cart();
                    c.setUsername(user.getUsername());
                    return c;
                });

        Optional<CartItem> itemOpt = cart.getItems()
                .stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst();

        if (itemOpt.isPresent()) {
            CartItem item = itemOpt.get();
            item.setQuantity(item.getQuantity() + 1);
        } else {
            cart.getItems().add(new CartItem(productId, 1));
        }

        cartRepository.save(cart);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(@AuthenticationPrincipal UserDetails user) {
        Cart cart = cartRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().clear();
        cartRepository.save(cart);
        return ResponseEntity.ok("Cart cleared");
    }
}
