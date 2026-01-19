import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { State } from '../model/state';
import { Product } from '../model/product'; // ✅ Import Product model

@Injectable({
  providedIn: 'root'
})
export class StateService {
  private statesApiUrl = 'http://localhost:8095/api/states';
  private productsApiUrl = 'http://localhost:8095/api/products'; // ✅ Product endpoint

  constructor(private http: HttpClient) {}

  getStates(): Observable<State[]> {
    return this.http.get<State[]>(this.statesApiUrl);
  }

getProductsByState(stateName: string): Observable<Product[]> {
  return this.http.get<Product[]>(`${this.productsApiUrl}?stateName=${stateName}`);

}
}