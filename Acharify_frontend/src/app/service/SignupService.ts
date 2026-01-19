import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface SignupData {
  username: string;       // ✅ new
  password: string;
  email: string;
  fullName: string;
  mobile: string;
  agreeToTerms: boolean;
}


@Injectable({
  providedIn: 'root'
})
export class SignupService {
  private apiUrl = 'http://localhost:8095/api/auth/signup'; // Replace with your API URL

  constructor(private http: HttpClient) { }

  signup(data: SignupData): Observable<any> {
    return this.http.post<any>(this.apiUrl, data);
  }

 
}
