import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

export interface LoginData {
  usernameOrEmail: string;
  password: string;
}

export interface LoginResponse {
  accessToken: string;
  tokenType: string;
  role: 'ROLE_ADMIN' | 'ROLE_USER' | 'ROLE_SUPPLIER';
  username: string;
  supplierId?: number; // only for supplier
}

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private apiUrl = 'http://localhost:8095/api/auth/signin';

  constructor(private http: HttpClient) {}

  login(data: LoginData): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(this.apiUrl, data).pipe(
      tap(res => {
        // Save token and user info
        localStorage.setItem('accessToken', res.accessToken);
        localStorage.setItem('role', res.role);
        localStorage.setItem('userName', res.username);

        if (res.role === 'ROLE_SUPPLIER' && res.supplierId) {
          localStorage.setItem('supplierId', res.supplierId.toString());
        }
      })
    );
  }

  logout() {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('role');
    localStorage.removeItem('userName');
    localStorage.removeItem('supplierId');
  }
}
