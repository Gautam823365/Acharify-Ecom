import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, forkJoin, Observable, of } from 'rxjs';
import { map, switchMap, tap } from 'rxjs/operators';
import { Product } from '../model/product';
import { CartItem } from '../model/CartItem';

@Injectable({
  providedIn: 'root',
})
export class CartService {

  private apiUrl = 'http://localhost:8095/api/cart';
  private productsUrl = 'http://localhost:8095/api/products';

  private cartCountSubject = new BehaviorSubject<number>(0);
  cartCount$ = this.cartCountSubject.asObservable();

  constructor(private http: HttpClient) {
    this.syncCartCount(); // ✅ fixes browser back button issue
  }

  // 🔹 Get full cart
  getCart(): Observable<{ items: CartItem[] }> {
    return this.http.get<{ items: CartItem[] }>(
      this.apiUrl,
      this.getAuthHeaders()
    );
  }

  // 🔹 Load cart products WITH quantity
  getCartProducts(): Observable<
    { product: Product; quantity: number }[]
  > {
    return this.getCart().pipe(
      switchMap(cart => {
        if (!cart.items || cart.items.length === 0) {
          return of([]);
        }

        const requests = cart.items.map(item =>
          this.http
            .get<Product>(
              `${this.productsUrl}/${item.productId}`,
              this.getAuthHeaders()
            )
            .pipe(
              map(product => ({
                product,
                quantity: item.quantity
              }))
            )
        );

        return forkJoin(requests);
      })
    );
  }

  // 🔹 Add product (same product → quantity++)
  addToCart(productId: number): void {
    this.http
      .post<any>(
        `${this.apiUrl}/add?productId=${productId}`,
        {},
        this.getAuthHeaders()
      )
      .pipe(
        tap(cart => this.updateCartCount(cart.items))
      )
      .subscribe();
  }

  // 🔹 Clear cart
  clearCart(): void {
    this.http
      .delete(
        `${this.apiUrl}/clear`,
        this.getAuthHeaders()
      )
      .subscribe(() => {
        this.cartCountSubject.next(0);
      });
  }

  // 🔹 Sync cart count (important for back button)
  syncCartCount(): void {
    this.getCart().subscribe(cart => {
      this.updateCartCount(cart.items);
    });
  }

  // 🔹 Count = sum of quantities
  updateCartCount(items: CartItem[]) {
    const count = items?.reduce(
      (sum, item) => sum + item.quantity,
      0
    ) ?? 0;

    this.cartCountSubject.next(count);
  }

  // 🔹 JWT headers
  private getAuthHeaders() {
    const token = localStorage.getItem('token');
    return {
      headers: new HttpHeaders({
        Authorization: `Bearer ${token}`,
      }),
    };
  }

decreaseQuantity(productId: number): void {
  this.http
    .put(
      `${this.apiUrl}/decrease?productId=${productId}`,
      {},
      this.getAuthHeaders()
    )
    .subscribe(cart => this.syncCartCount());
}

removeItemCompletely(productId: number): void {
  this.http
    .delete(
      `${this.apiUrl}/remove/${productId}`,
      this.getAuthHeaders()
    )
    .subscribe(cart => this.syncCartCount());
}
 

}