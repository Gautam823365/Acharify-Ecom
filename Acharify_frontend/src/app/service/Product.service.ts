import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

/**
 * 🔹 Backend Product DTO (Spring Boot)
 */
export interface ProductPayload {
  productsId: number;
  productsName: string;
  productsDescription?: string;
  productsCategoryId: number;
  productsUnitPrice: number;
  productsQuantity: number;
  productsThreshold: number;
  productsImage?: string;
  stateName: string;
}

/**
 * 🔹 UI Product Model (used in dashboard)
 */
export interface ProductUI {
  id: number;
  name: string;
  category: string;
  price: number;
  quantity: number;
  threshold: number;
  unit: string;
  description?: string;
  image?: string;
}

@Injectable({
  providedIn: 'root'
})
export class ProductsService {

  private readonly apiUrl = 'http://localhost:8095/api/products';

  constructor(private http: HttpClient) {}

  // ➕ CREATE product (Supplier only)
 // ✅ MULTIPART UPLOAD (IMAGE + DATA)
  addProduct(formData: FormData): Observable<any> {
    return this.http.post(this.apiUrl, formData);
  }

    uploadImage(formData: FormData): Observable<{ url: string }> {
    return this.http.post<{ url: string }>(
      `${this.apiUrl}/upload-image`,
      formData
    );
  }

  // ✏️ UPDATE product
  updateProduct(id: number, payload: ProductPayload): Observable<ProductPayload> {
    return this.http.put<ProductPayload>(`${this.apiUrl}/${id}`, payload);
  }

  // 📦 GET products by supplier
  getProductsBySupplier(supplierId: number): Observable<ProductPayload[]> {
    return this.http.get<ProductPayload[]>(
      `${this.apiUrl}/supplier/${supplierId}`
    );
  }

  // ❌ DELETE product
  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
