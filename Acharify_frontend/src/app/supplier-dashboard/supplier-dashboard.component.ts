import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProductPayload, ProductsService } from '../service/Product.service';


interface Product {
  id: number;
  name: string;
  category: string;
  price: number;
  quantity: number;
  threshold: number;
  unit: string;
  weight?: string;
  description?: string;
  image?: string;
}

@Component({
  selector: 'app-supplier-products',
  templateUrl: './supplier-dashboard.component.html',
  styleUrls: ['./supplier-dashboard.component.css'],
  standalone:true,
  imports: [
    CommonModule,          // ✅ ngClass, ngIf, ngFor
    ReactiveFormsModule    // ✅ formGroup, formControlName
  ]
})
export class SupplierProductsComponent implements OnInit {
  productForm!: FormGroup;
  products: Product[] = [];
  currentImage: string | null = null;
  editingProductId: number | null = null;
  showStockWarning: boolean = false;
 // currentImage: string | null = null;        // preview
  currentImageFile: File | null = null;  
  mapCategoryToId(category: string): number {
  const categoryMap: { [key: string]: number } = {
    Fruit: 1,
    Vegetable: 2,
    Mixed: 3,
    Spicy: 4,
    Sweet: 5
  };

  return categoryMap[category] ?? 0;
}

categories = [
  { value: 'Fruit', label: 'Fruit Pickle', id: 1 },
  { value: 'Vegetable', label: 'Vegetable Pickle', id: 2 },
  { value: 'Mixed', label: 'Mixed Pickle', id: 3 },
  { value: 'Spicy', label: 'Spicy Pickle', id: 4 },
  { value: 'Sweet', label: 'Sweet Pickle', id: 5 }
];

  units = [
    { value: 'grams', label: 'Grams' },
    { value: 'kg', label: 'Kilograms' },
    { value: 'pieces', label: 'Pieces' },
    { value: 'bottles', label: 'Bottles' },
    { value: 'jars', label: 'Jars' }
  ];

  constructor(private fb: FormBuilder ,private productsService: ProductsService) {}

  ngOnInit(): void {
    this.initializeForm();
    this.loadProductsFromApi();
  }

  initializeForm(): void {
    this.productForm = this.fb.group({
      name: ['', Validators.required],
      category: ['', Validators.required],
      price: ['', [Validators.required, Validators.min(1)]],
      quantity: ['', [Validators.required, Validators.min(0)]],
      threshold: ['', [Validators.required, Validators.min(1)]],
      unit: ['', Validators.required],
      weight: [''],
      description: ['']
    });

    // Watch for stock changes
    this.productForm.get('quantity')?.valueChanges.subscribe(() => {
      this.checkStockWarning();
    });
    this.productForm.get('threshold')?.valueChanges.subscribe(() => {
      this.checkStockWarning();
    });
  }

 loadProductsFromApi(): void {
  const supplierId = Number(localStorage.getItem('supplierId'));

  this.productsService.getProductsBySupplier(supplierId).subscribe({
    next: (data: ProductPayload[]) => {
      this.products = data as any;
    },
    error: (err: any) => {
      console.error('Failed to load products', err);
    }
  });



}




  saveProducts(): void {
    // Save to local storage or send to API
    localStorage.setItem('supplierProducts', JSON.stringify(this.products));
  }

onImageUpload(event: Event): void {
  const input = event.target as HTMLInputElement;
  if (!input.files || input.files.length === 0) return;

  const file = input.files[0];
  this.currentImageFile = file;

  // preview only
  const reader = new FileReader();
  reader.onload = () => {
    this.currentImage = reader.result as string;
  };
  reader.readAsDataURL(file);
}
onSubmit(): void {
  if (this.productForm.invalid || !this.currentImageFile) {
    alert('Image is required');
    return;
  }

  const formData = new FormData();

  // 🔑 MUST MATCH @RequestParam NAMES EXACTLY
  formData.append('productsName', this.productForm.value.name);
  formData.append('productsDescription', this.productForm.value.description || '');
  formData.append(
    'productsCategoryId',
    this.mapCategoryToId(this.productForm.value.category).toString()
  );
  formData.append('productsUnitPrice', this.productForm.value.price.toString());
  formData.append('productsQuantity', this.productForm.value.quantity.toString());
  formData.append('productsThreshold', this.productForm.value.threshold.toString());
  formData.append('stateName', 'ACTIVE');
  formData.append('image', this.currentImageFile);

  this.productsService.addProduct(formData).subscribe({
    next: () => {
      alert('Product added successfully');
      this.resetForm();
      this.loadProductsFromApi();
    },
    error: err => {
      console.error('Upload failed', err);
      alert('Upload failed');
    }
  });
}


// onImageUrlChange(event: Event): void {
//   const input = event.target as HTMLInputElement;
//   this.currentImage = input.value || null; // ✅ URL only
// }

  checkStockWarning(): void {
    const quantity = this.productForm.get('quantity')?.value || 0;
    const threshold = this.productForm.get('threshold')?.value || 0;
    this.showStockWarning = quantity > 0 && quantity <= threshold;
  }

  getStockStatus(product: Product): string {
    if (product.quantity === 0) return 'out';
    if (product.quantity <= product.threshold) return 'low';
    return 'high';
  }

  getStockLabel(product: Product): string {
    const status = this.getStockStatus(product);
    if (status === 'out') return 'Out of Stock';
    if (status === 'low') return 'Low Stock';
    return 'In Stock';
  }

//  onSubmit(): void {
//   if (this.productForm.invalid) {
//     this.productForm.markAllAsTouched();
//     return;
//   }

//   const payload: ProductPayload = {
//     productsName: this.productForm.value.name,
//     productsDescription: this.productForm.value.description,
//     productsCategoryId: this.mapCategoryToId(this.productForm.value.category),
//     productsUnitPrice: this.productForm.value.price,
//     productsQuantity: this.productForm.value.quantity,
//     productsThreshold: this.productForm.value.threshold,
//     productsImage: this.productForm.value.imageUrl || undefined, // ✅ URL
//   //  productsSupplierId: Number(localStorage.getItem('supplierId')),
//     stateName: 'ACTIVE',
//     productsId: 0
//   };

//   if (this.isEditing) {
//     this.productsService
//       .updateProduct(this.editingProductId!, payload)
//       .subscribe({
//         next: () => {
//           alert('Product updated successfully');
//           this.resetForm();
//           this.loadProductsFromApi();
//         },
//         error: err => console.error(err)
//       });
//   } else {
//     this.productsService
//       .addProduct(payload)
//       .subscribe({
//         next: () => {
//           alert('Product added successfully');
//           this.resetForm();
//           this.loadProductsFromApi();
//         },
//         error: err => console.error(err)
//       });
//   }
// }

  editProduct(product: Product): void {
    this.editingProductId = product.id;
    this.currentImage = product.image || null;
    
    this.productForm.patchValue({
      name: product.name,
      category: product.category,
      price: product.price,
      quantity: product.quantity,
      threshold: product.threshold,
      unit: product.unit,
      weight: product.weight || '',
      description: product.description || ''
    });

    this.checkStockWarning();
    
    // Scroll to form
    setTimeout(() => {
      document.querySelector('.form-section')?.scrollIntoView({ behavior: 'smooth' });
    }, 100);
  }

 deleteProduct(id: number | undefined): void {
  if (!id) {
    alert('Invalid product ID');
    return;
  }

  if (!confirm('Are you sure?')) return;

  this.productsService.deleteProduct(id).subscribe({
    next: () => this.loadProductsFromApi(),
    error: () => alert('Delete failed')
  });
}





  resetForm(): void {
    this.productForm.reset();
    this.currentImage = null;
    this.editingProductId = null;
    this.showStockWarning = false;
  }

  get isEditing(): boolean {
    return this.editingProductId !== null;
  }

  triggerFileInput(): void {
    document.getElementById('imageInput')?.click();
  }
 
logout(): void {
  // Clear any authentication tokens or user data
  localStorage.removeItem('token'); // if you store JWT or session token
  localStorage.removeItem('user');  // optional, user info

  // Redirect to login page
  window.location.href = '/login'; // or use Angular Router if you have one
}
  
}
