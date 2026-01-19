import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { Router } from '@angular/router';
import { LoginData, LoginResponse, LoginService } from '../service/login.service';

@Component({
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule],
  selector: 'app-login',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  showPassword = false;
  isSubmitting = false;
 selectedRole: 'user' | 'supplier' = 'user';

  constructor(private fb: FormBuilder, private loginService: LoginService, private router: Router) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {}

 

   toggleRole(): void {
    this.selectedRole = this.selectedRole === 'user' ? 'supplier' : 'user';
    console.log('Selected role:', this.selectedRole);
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }
  getErrorMessage(controlName: string): string {
    const control = this.loginForm.get(controlName);
    if (!control || !control.errors || !control.touched) return '';

    if (controlName === 'email') {
      if (control.errors['required']) return 'Email is required';
      if (control.errors['email']) return 'Please enter a valid email address';
    }

    if (controlName === 'password') {
      if (control.errors['required']) return 'Password is required';
    }

    return '';
  }
onSubmit(): void {
  if (this.loginForm.valid) {
    this.isSubmitting = true;

    const loginData: LoginData = {
      usernameOrEmail: this.loginForm.value.email,
      password: this.loginForm.value.password
    };

    this.loginService.login(loginData).subscribe({
    next: (response: LoginResponse) => {
  console.log('Login response:', response);

  localStorage.setItem('token', response.accessToken);
  localStorage.setItem('userName', response.username);
  localStorage.setItem('role', response.role);
  localStorage.setItem('supplierId',response.supplierId?.toString()||' ');
console.log('Stored supplierId:', localStorage.getItem('supplierId'));

  switch (response.role) {
  case 'ROLE_ADMIN':
    this.router.navigate(['/admindashboard']);
    break;

  case 'ROLE_SUPPLIER':
    // Store supplierId safely
    if (response.supplierId !== undefined) {
      localStorage.setItem('supplierId', response.supplierId.toString());
    } else {
      console.warn('supplierId is missing for supplier!');
    }
    this.router.navigate(['/supplier-products']);
    break;

  case 'ROLE_USER':
    this.router.navigate(['/welcome-dash-board']);
    break;

  default:
    console.error('Unknown role:', response.role);
    this.router.navigate(['/login']);
}


  this.isSubmitting = false;


},
      error: (error) => {
        console.error('Login failed', error);
        alert('Login failed. Please check your credentials and try again.');
        this.isSubmitting = false;
      }
    });
  } else {
    Object.keys(this.loginForm.controls).forEach(key => {
      this.loginForm.get(key)?.markAsTouched();
    });
  }
}
}