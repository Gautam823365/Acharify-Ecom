import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { CartService } from '../service/cart.service';
import { Product } from '../model/product';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css'],
})
export class CartComponent implements OnInit {

  cartItems: { product: Product; quantity: number }[] = [];

  constructor(
    private cartService: CartService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadCart();
  }

  loadCart(): void {
    this.cartService.getCartProducts().subscribe(items => {
      this.cartItems = items;
    });
  }

  // ✅ Increase quantity (backend synced)
  increaseQuantity(item: { product: Product; quantity: number }): void {
    this.cartService.addToCart(item.product.productsId!);
    this.loadCart();
  }

  // ✅ Decrease quantity (backend required)
  decreaseQuantity(item: { product: Product; quantity: number }): void {
    this.cartService.decreaseQuantity(item.product.productsId!);
    this.loadCart();
  }

  // ✅ Remove item completely
  removeItem(productId: number): void {
    this.cartService.removeItemCompletely(productId);
    this.loadCart();
  }

  clearCart(): void {
    this.cartService.clearCart();
    this.cartItems = [];
  }

  // ✅ Correct total calculation
  getTotal(): number {
    return this.cartItems.reduce(
      (sum, item) =>
        sum + (item.product.productsUnitPrice ?? 0) * item.quantity,
      0
    );
  }

  proceedToCart(): void {
    this.router.navigate(['/checkout'], {
      state: {
        items: this.cartItems,
        total: this.getTotal(),
      },
    });
  }
}
