package com.example.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.user.entity.Cart;
import com.example.user.entity.CartItem;
import com.example.user.repo.CartRepository;

@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    // 🔹 Get cart
    public Cart getCart(String username) {
        return cartRepository.findByUsername(username)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUsername(username);
                    return cartRepository.save(cart);
                });
    }

    // 🔹 Add to cart (increase qty if exists)
    public Cart addToCart(String username, Long productId) {
        Cart cart = getCart(username);

        Optional<CartItem> existingItem = cart.getItems()
                .stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(
                    existingItem.get().getQuantity() + 1
            );
        } else {
            cart.getItems().add(new CartItem(productId, 1));
        }

        return cartRepository.save(cart);
    }

    // 🔹 Remove one quantity
    public Cart removeOne(String username, Long productId) {
        Cart cart = getCart(username);

        cart.getItems().removeIf(item -> {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(item.getQuantity() - 1);
                return item.getQuantity() <= 0;
            }
            return false;
        });

        return cartRepository.save(cart);
    }

    // 🔹 Clear cart
    public void clearCart(String username) {
        Cart cart = getCart(username);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    // 🔹 Cart count (IMPORTANT)
    public int getCartCount(String username) {
        Cart cart = getCart(username);
        return cart.getItems()
                .stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}
