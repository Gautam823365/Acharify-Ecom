// import { Injectable } from '@angular/core';
// import { HttpClient } from '@angular/common/http';
// import { map, Observable } from 'rxjs';

// export interface Address {
//   street: string;
//   city: string;
//   state: string;
//   zip: string;
// }

// export interface UserProfile {
//   name: string;
//   email: string;
//   phoneNumber: string;
//   profileImage?: string;
//   address: Address;
// }

// @Injectable({ providedIn: 'root' })
// export class UserProfileService {
//   private apiUrl = 'http://localhost:8095/api/auth/me';

//   constructor(private http: HttpClient) {}

//   getUserProfile(): Observable<UserProfile> {
//     return this.http.get<UserProfile>(this.apiUrl);
//   }

//  updateUserProfile(user: UserProfile): Observable<any> {
//   const payload = {
//     fullName: user.name,            // ✅ backend expects fullName
//     email: user.email,
//     phoneNumber: user.phoneNumber,
//     profileImage: user.profileImage,
//     address: {
//       street: user.address?.street || '',
//       city: user.address?.city || '',
//       state: user.address?.state || '',
//       zip: user.address?.zip || '',
//     },
//   };

//   return this.http.put<any>(this.apiUrl, payload);
// }

// // In userProfile.service.ts
// uploadProfileImage(formData: FormData) {
//   return this.http.post<{ success: boolean; message: string; imageUrl: string }>(
//     'http://localhost:8095/api/auth/me/upload-avatar',
//     formData
//   ).pipe(
//     map(res => ({
//       ...res,
//       imageUrl: 'http://localhost:8095' + res.imageUrl   // prepend backend URL
//     }))
//   );
// }
// }



import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';

export interface Address {
  street: string;
  city: string;
  state: string;
  zip: string;
}

export interface UserProfile {
  name: string;
  email: string;
  phoneNumber: string;
  profileImage?: string;
  address: Address;
}

@Injectable({ providedIn: 'root' })
export class UserProfileService {
  private apiUrl = 'http://localhost:8095/api/auth/me';
  private readonly API_BASE_URL = 'http://localhost:8095';

  constructor(private http: HttpClient) {}

  getUserProfile(): Observable<UserProfile> {
    return this.http.get<UserProfile>(this.apiUrl).pipe(
      map(user => {
        return {
          ...user,
          profileImage: user.profileImage
            ? (user.profileImage.startsWith('http')
                ? user.profileImage
                : this.API_BASE_URL + user.profileImage)
            : 'assets/default-avatar.png',
        };
      })
    );
  }

  updateUserProfile(user: UserProfile): Observable<any> {
    const payload = {
      fullName: user.name,
      email: user.email,
      phoneNumber: user.phoneNumber,
      profileImage: user.profileImage,
      address: {
        street: user.address?.street || '',
        city: user.address?.city || '',
        state: user.address?.state || '',
        zip: user.address?.zip || '',
      },
    };

    return this.http.put<any>(this.apiUrl, payload);
  }

  uploadProfileImage(formData: FormData): Observable<string> {
    return this.http.post<{ success: boolean; message: string; imageUrl: string }>(
      this.API_BASE_URL + '/api/auth/me/upload-avatar',
      formData
    ).pipe(
      map(res => {
        if (res.imageUrl.startsWith('http')) {
          return res.imageUrl;
        }
        return this.API_BASE_URL + res.imageUrl;
      })
    );
  }
}
