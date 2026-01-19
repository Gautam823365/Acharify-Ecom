import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { AdminService, Supplier as ApiSupplier } from '../service/admin.service';

// Frontend interface for UI
interface Supplier {
  supplierId?: number; 
  ownerName: string;
  shopName: string;
  email: string;
  phone: string;
  gstinNumber: string;
  address: string;
  city: string;
  state: string;
  pincode: string;
  status: 'active' | 'inactive';
  createdDate?: Date;
  password?: string;
  

}

@Component({
  selector: 'app-admin-supplier-dashboard',
  templateUrl: './admindashboard.component.html',
  styleUrls: ['./admindashboard.component.css'],
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule]
})
export class AdminSupplierDashboardComponent implements OnInit {
  supplierForm!: FormGroup;
  suppliers: Supplier[] = [];
  editingSupplier: Supplier | null = null;
  showForm: boolean = false;
  searchTerm: string = '';
  filterStatus: string = 'all';
  newPassword: string = '';
selectedSupplier?: Supplier;
showPassword: boolean = false;

  states = [
    'Andhra Pradesh', 'Arunachal Pradesh', 'Assam', 'Bihar', 'Chhattisgarh',
    'Goa', 'Gujarat', 'Haryana', 'Himachal Pradesh', 'Jharkhand', 'Karnataka',
    'Kerala', 'Madhya Pradesh', 'Maharashtra', 'Manipur', 'Meghalaya', 'Mizoram',
    'Nagaland', 'Odisha', 'Punjab', 'Rajasthan', 'Sikkim', 'Tamil Nadu',
    'Telangana', 'Tripura', 'Uttar Pradesh', 'Uttarakhand', 'West Bengal'
  ];

  constructor(private fb: FormBuilder, private adminService: AdminService) {}

  ngOnInit(): void {
    this.initializeForm();
    this.loadSuppliers();
  }

  initializeForm(): void {
    this.supplierForm = this.fb.group({
      ownerName: ['', [Validators.required, Validators.minLength(3)]],
      shopName: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern(/^[0-9]{10}$/)]],
      gstinNumber: ['', [Validators.required, Validators.pattern(/^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$/)]],
      address: ['', [Validators.required, Validators.minLength(10)]],
      city: ['', Validators.required],
      state: ['', Validators.required],
      pincode: ['', [Validators.required, Validators.pattern(/^[0-9]{6}$/)]],
      status: ['active'],
    password: ['', [Validators.minLength(6)]]
 
    });
  }

  loadSuppliers(): void {
    this.adminService.getSuppliers().subscribe({
      next: (data: ApiSupplier[]) => {
        this.suppliers = data.map(s => ({
          supplierId: s.supplierId,
           ownerName: s.suppliersName,
          shopName: s.supplierBusinessName,
          email: s.suppliersEmail,
          phone: s.suppliersPhone,
          gstinNumber: s.gstIdNo || '',
          address: String(s.suppliersAddressId || ''), // map your address if available
          city: s.city || '',
          state: s.state || '',
          pincode: s.pincode || '',
          status: (s.status || 'active') as 'active' | 'inactive',
          createdDate: new Date()

          
        }));
      },
      error: (err) => console.error('Error fetching suppliers', err)
    });
  }

  onSubmit(): void {
    if (this.supplierForm.invalid) {
      Object.keys(this.supplierForm.controls).forEach(key => {
        this.supplierForm.get(key)?.markAsTouched();
      });
      return;
    }
  //   if (!this.editingSupplier?.supplierId) {  
  // alert('Supplier ID missing. Cannot update.');
  // return;
//}

    const formValue = this.supplierForm.value;
const password = formValue.password;
    if (this.editingSupplier) {
      // Update supplier
      const updatedSupplier: ApiSupplier = {
        supplierId: this.editingSupplier.supplierId,
        suppliersName: formValue.ownerName,
        supplierBusinessName: formValue.shopName,
        suppliersEmail: formValue.email,
        suppliersPhone: formValue.phone,
        suppliersAddressId:formValue.address,
        gstIdNo: formValue.gstinNumber,
        city: formValue.city,
        state: formValue.state,
        pincode: formValue.pincode,
        status: formValue.status,
        id: undefined,
        password:this.newPassword||undefined
      };

      this.adminService.updateSupplier(this.editingSupplier.supplierId!, updatedSupplier).subscribe({
        next: () => {
          this.loadSuppliers();
          alert('Supplier updated successfully!');
          this.resetForm();
          this.showForm = false;
        },
        error: (err) => console.error('Error updating supplier', err)
      });
    } else {
      // Add supplier
      const newSupplier: ApiSupplier = {
        suppliersName: formValue.ownerName,
        supplierBusinessName: formValue.shopName,
        suppliersEmail: formValue.email,
        suppliersPhone: formValue.phone,
        suppliersAddressId:formValue.suppliersAddressId,
        gstIdNo: formValue.gstinNumber,
        city: formValue.city,
        state: formValue.state,
        pincode: formValue.pincode,
        status: formValue.status,
        id: undefined,
        password: this.newPassword || undefined // optional
      };

      this.adminService.addSupplier(newSupplier).subscribe({
        next: () => {
          this.loadSuppliers();
          alert('Supplier added successfully!');
          this.resetForm();
          this.showForm = false;
        },
        error: (err) => console.error('Error adding supplier', err)
      });
    }
  }
togglePassword(): void {
  this.showPassword = !this.showPassword;
}
  editSupplier(supplier: Supplier): void {
    this.editingSupplier = supplier;
    this.selectedSupplier = supplier; 
    this.showForm = true;

    this.supplierForm.patchValue({ ...supplier });

    setTimeout(() => {
      document.querySelector('.form-card')?.scrollIntoView({ behavior: 'smooth' });
    }, 100);
  }

  deleteSupplier(id: number): void {
    if (confirm('Are you sure you want to delete this supplier?')) {
      this.adminService.deleteSupplier(id).subscribe({
        next: () => {
          this.loadSuppliers();
          alert('Supplier deleted successfully!');
        },
        error: (err) => console.error('Error deleting supplier', err)
      });
    }
  }

  toggleStatus(supplier: Supplier): void {
    supplier.status = supplier.status === 'active' ? 'inactive' : 'active';
    if (!supplier.supplierId) return;

    const payload: ApiSupplier = {
      supplierId: supplier.supplierId,
      suppliersName: supplier.ownerName,
      supplierBusinessName: supplier.shopName,
      suppliersEmail: supplier.email,
      suppliersPhone: supplier.phone,
      suppliersAddressId: supplier.address,
      gstIdNo: supplier.gstinNumber,
      city: supplier.city,
      state: supplier.state,
      pincode: supplier.pincode,
      status: supplier.status,
      id: undefined
    };


    this.adminService.updateSupplier(supplier.supplierId, payload).subscribe(() => {
      this.loadSuppliers();
    });
  }
  resetForm(): void {
    this.supplierForm.reset({ status: 'active' });
    this.editingSupplier = null;
  }

  // Optional: Filtered suppliers
  get filteredSuppliers(): Supplier[] {
    return this.suppliers.filter(supplier => {
      const matchesSearch = 
        supplier.ownerName.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        supplier.shopName.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        supplier.email.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        supplier.phone.includes(this.searchTerm) ||
        supplier.gstinNumber.toLowerCase().includes(this.searchTerm.toLowerCase());
      
      const matchesStatus = this.filterStatus === 'all' || supplier.status === this.filterStatus;
      return matchesSearch && matchesStatus;
    });
  }

  // Optional: Dashboard statistics
  get statistics() {
    return {
      total: this.suppliers.length,
      active: this.suppliers.filter(s => s.status === 'active').length,
      inactive: this.suppliers.filter(s => s.status === 'inactive').length
    };
  }
  // Add inside AdminSupplierDashboardComponent class

// Toggle form visibility
toggleForm(): void {
  this.showForm = !this.showForm;
  if (!this.showForm) {
    this.resetForm();
  }
}

// Getter to check if editing
get isEditing(): boolean {
  return this.editingSupplier !== null;
}

// Export suppliers to CSV
exportToCSV(): void {
  if (!this.suppliers.length) return;

  const headers = ['Owner Name', 'Shop Name', 'Email', 'Phone','SupplierId', 'GSTIN', 'Address', 'City', 'State', 'Pincode', 'Status'];
  const rows = this.suppliers.map(s => [
    s.ownerName, s.shopName, s.email, s.phone, s.supplierId,s.gstinNumber,
    s.address, s.city, s.state, s.pincode, s.status
  ]);

  let csv = headers.join(',') + '\n';
  rows.forEach(row => {
    csv += row.map(cell => `"${cell}"`).join(',') + '\n';
  });

  const blob = new Blob([csv], { type: 'text/csv' });
  const url = window.URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = 'suppliers.csv';
  a.click();
}

// Update deleteSupplier to handle undefined i

logout(): void {
  // Clear any authentication tokens or user data
  localStorage.removeItem('token'); // if you store JWT or session token
  localStorage.removeItem('user');  // optional, user info

  // Redirect to login page
  window.location.href = '/login'; // or use Angular Router if you have one
}


// resetPassword(supplier: Supplier): void {
//   if (!supplier?.supplierId) {
//     alert('Supplier ID missing');
//     return;
//   }

//   const password = this.supplierForm.get('password')?.value;

//   if (!password || password.length < 6) {
//     alert('Password must be at least 6 characters');
//     return;
//   }

//   this.adminService
//     .setSupplierPassword(supplier.supplierId, password)
//     .subscribe({
//       next: () => {
//         alert('Password updated successfully');
//         this.supplierForm.get('password')?.reset();
//       },
//       error: () => alert('Password update failed')
//     });
// }


}
