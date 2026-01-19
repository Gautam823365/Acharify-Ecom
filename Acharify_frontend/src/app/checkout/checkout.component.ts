import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CartService } from '../service/cart.service';

// Angular Material
import { MatCardModule } from '@angular/material/card';
import { MatRadioModule } from '@angular/material/radio';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatNativeDateModule } from '@angular/material/core';

import { Product } from '../model/product';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatRadioModule,
    MatFormFieldModule,
    MatDatepickerModule,
    MatInputModule,
    MatButtonModule,
    MatNativeDateModule
  ],
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {

  cartItems: { product: Product; quantity: number }[] = [];

  subtotal = 0;
  shipping = 50;
  total = 0;

  deliveryDate!: string;
  paymentMethod = 'cod';

  paymentOptions = [
    { value: 'upi', label: 'UPI' },
    { value: 'card', label: 'Credit / Debit Card' },
    { value: 'cod', label: 'Cash on Delivery' },
    { value: 'netbanking', label: 'Net Banking' }
  ];

  constructor(private cartService: CartService) {}

  ngOnInit(): void {
    this.deliveryDate = this.getDefaultDeliveryDate();
    this.loadCartProducts();
  }

  getDefaultDeliveryDate(): string {
    const date = new Date();
    date.setDate(date.getDate() + 7);
    return date.toISOString().split('T')[0];
  }

  loadCartProducts(): void {
    this.cartService.getCartProducts().subscribe({
      next: items => {
        this.cartItems = items;
        this.updateTotals();
      },
      error: err => console.error('Failed to load cart', err)
    });
  }

  increaseQuantity(item: { product: Product; quantity: number }): void {
    this.cartService.addToCart(item.product.productsId!);
    this.loadCartProducts();
  }

  decreaseQuantity(item: { product: Product; quantity: number }): void {
    this.cartService.decreaseQuantity(item.product.productsId!);
    this.loadCartProducts();
  }

  updateTotals(): void {
    this.subtotal = this.cartItems.reduce(
      (sum, item) =>
        sum + (item.product.productsUnitPrice ?? 0) * item.quantity,
      0
    );
    this.total = this.subtotal + this.shipping;
  }

  proceedToPay(): void {
    if (!this.deliveryDate) {
      alert('Please select a delivery date!');
      return;
    }

    const request = {
      deliveryDate: this.deliveryDate,
      paymentMethod: this.paymentMethod,
      totalAmount: this.total
    };

    console.log('Checkout request:', request);

    // ✅ Call backend checkout API here
    // this.checkoutService.checkout(request).subscribe(...);

    alert(`Order placed successfully! Total: ₹${this.total}`);
  }
}
