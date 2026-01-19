import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ReactiveFormsModule } from '@angular/forms';
import { SignupData, SignupService } from '../service/SignupService';
import { HttpClientModule } from '@angular/common/http';

@Component({
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule,HttpClientModule],
  selector: 'app-signup-page',
  templateUrl: './signup-page.component.html',
  styleUrls: ['./signup-page.component.css']
})
export class SignupComponent implements OnInit {
  signupForm: FormGroup;
  showPassword = false;
  showConfirmPassword = false;
  isSubmitting = false;
  router: any;

  constructor(private formBuilder: FormBuilder, private signupService: SignupService) {
    this.signupForm = this.formBuilder.group({
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      phonenumber: ['', [
        Validators.required,
        Validators.pattern(/^\+?[0-9]{10,15}$/)  // Phone validation pattern (10-15 digits, optional +)
      ]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8), this.passwordValidator]],
      confirmPassword: ['', [Validators.required]],
      agreeToTerms: [false, [Validators.requiredTrue]]
    }, { validators: this.passwordMatchValidator });
  }

  ngOnInit(): void {}

  passwordValidator(control: AbstractControl): { [key: string]: any } | null {
    const password = control.value;
    if (!password) return null;

    const hasNumber = /[0-9]/.test(password);
    const hasUpper = /[A-Z]/.test(password);
    const hasLower = /[a-z]/.test(password);
    const hasSpecial = /[#?!@$%^&*-]/.test(password);

    const valid = hasNumber && hasUpper && hasLower && hasSpecial;
    return valid ? null : { 'passwordStrength': true };
  }

  passwordMatchValidator(form: AbstractControl): { [key: string]: any } | null {
    const password = form.get('password');
    const confirmPassword = form.get('confirmPassword');

    if (!password || !confirmPassword) return null;

    return password.value !== confirmPassword.value ? { 'passwordMismatch': true } : null;
  }

  togglePasswordVisibility(field: 'password' | 'confirmPassword'): void {
    if (field === 'password') {
      this.showPassword = !this.showPassword;
    } else {
      this.showConfirmPassword = !this.showConfirmPassword;
    }
  }

  getErrorMessage(controlName: string): string {
    const control = this.signupForm.get(controlName);
    if (!control || !control.errors || !control.touched) return '';

    const errors = control.errors;

    switch (controlName) {
      case 'firstName':
      case 'lastName':
        if (errors['required']) return `${controlName === 'firstName' ? 'First' : 'Last'} name is required`;
        if (errors['minlength']) return `${controlName === 'firstName' ? 'First' : 'Last'} name must be at least 2 characters`;
        break;
      case 'email':
        if (errors['required']) return 'Email is required';
        if (errors['email']) return 'Please enter a valid email address';
        break;
      case 'password':
        if (errors['required']) return 'Password is required';
        if (errors['minlength']) return 'Password must be at least 8 characters';
        if (errors['passwordStrength']) return 'Password must contain uppercase, lowercase, number, and special character';
        break;
      case 'confirmPassword':
        if (errors['required']) return 'Please confirm your password';
        if (this.signupForm.errors?.['passwordMismatch']) return 'Passwords do not match';
        break;
      case 'agreeToTerms':
        if (errors['required']) return 'You must agree to the terms and conditions';
        break;
    }
    return '';
  }

  onSubmit(): void {
  if (this.signupForm.valid) {
    this.isSubmitting = true;

    const formValues = this.signupForm.value;

    const signupData: SignupData = {
      username: formValues.email, // ✅ Set username as email
      email: formValues.email,
      password: formValues.password,
      fullName: `${formValues.firstName} ${formValues.lastName}`,
      mobile: formValues.phonenumber,
      agreeToTerms: formValues.agreeToTerms
    };

    this.signupService.signup(signupData).subscribe({
      next: (response) => {
        alert('Account created successfully!');

        this.signupForm.reset();
        this.isSubmitting = false;
          this.router.navigate(['/login']);
      },
      error: (error) => {
        console.error('Signup failed', error);
        alert('Signup failed. Please try again.');
        this.isSubmitting = false;
      }
    });

  } else {
    Object.keys(this.signupForm.controls).forEach(key => {
      this.signupForm.get(key)?.markAsTouched();
    });
  }
}


  signupWithGoogle(): void {
    console.log('Google signup clicked');
    // TODO: Implement Google OAuth signup logic here
  }


}
