import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

/* ===== Models ===== */

export interface Cart {
  items: Cart;
  id: number;
  username: string;
  productIds: number[];
}

export interface CheckoutRequest {
  deliveryDate: string | null;   // yyyy-MM-dd
  paymentMethod: string;
  totalAmount: number;
}

export interface CheckoutResponse {
  orderId: number;
  status: string;
  message: string;
}


export interface CartItem {
  productsId: number;
  productsName: string;
  productsUnitPrice: number;
  quantity: number;
}
/* ===== Service ===== */

@Injectable({
  providedIn: 'root'
})
export class CheckoutService {

  private baseUrl = 'http://localhost:8094/api/cart';

  constructor(private http: HttpClient) {}

  // 1️⃣ Get cart
 getCart(): Observable<CartItem[]> {
  return this.http.get<CartItem[]>(`${this.baseUrl}`);
}


  // 2️⃣ Checkout
  checkout(request: CheckoutRequest): Observable<CheckoutResponse> {
    return this.http.post<CheckoutResponse>(
      `${this.baseUrl}/checkout`,
      request
    );
  }
}
