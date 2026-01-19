import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

// Supplier interface matching your Spring Boot entity
export interface Supplier {
  id: any;
  supplierId?: number;
  suppliersName: string;
  supplierBusinessName: string;
  suppliersPhone: string;
  suppliersEmail: string;
  suppliersAddressId?: string;
  gstIdNo?: string;
  city?: string;
  pincode?: string;
  state?: string;
  status?: string; 
  password?:string// ACTIVE / INACTIVE
}

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  private baseUrl = 'http://localhost:8095/api/suppliers'; // Update with your backend URL

  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) { }


  private getAuthHeaders() {
  const token = localStorage.getItem('token');
  return {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`
    })
  };
}

  // GET all suppliers
  getSuppliers(): Observable<Supplier[]> {
    return this.http.get<Supplier[]>(this.baseUrl);
  }

  // GET supplier by ID
  getSupplierById(id: number): Observable<Supplier> {
    return this.http.get<Supplier>(`${this.baseUrl}/${id}`);
  }

  // POST - add new supplier
  addSupplier(supplier: Supplier): Observable<Supplier> {
    return this.http.post<Supplier>(this.baseUrl, supplier, this.httpOptions);
  }

  // PUT - update supplier
 updateSupplier(id: number, supplier: Supplier): Observable<Supplier> {
  return this.http.put<Supplier>(
    `${this.baseUrl}/${id}`,
    supplier,
    this.getAuthHeaders()
  );
}


  // DELETE - remove supplier
  deleteSupplier(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`, this.httpOptions);
  }

//   setSupplierPassword(id: number, password: string): Observable<any> {
//   return this.http.put(`${this.baseUrl}/${id}/password`, { password }, this.httpOptions);
// }
setSupplierPassword(id: number, password: string) {
  return this.http.put(
    `${this.baseUrl}/suppliers/${id}/password`,
    { password }
  );
}

}