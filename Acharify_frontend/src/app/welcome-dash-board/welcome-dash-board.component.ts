import {
  Component,
  Inject,
  OnInit,
  PLATFORM_ID,
  ElementRef,
  HostListener,
} from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { ChunkPipe } from '../pipe/chunk.pipe';
import { StateService } from '../services/state.service';
import { State } from '../model/state';
import { Product } from '../model/product';
import { HttpClientModule } from '@angular/common/http';
import { UserProfileService, UserProfile } from '../service/userProfile.service';
import { CartService } from '../service/cart.service';
import { NavigationEnd, Route, Router } from '@angular/router';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-welcome-dash-board',
  standalone: true,
  imports: [CommonModule, ChunkPipe, HttpClientModule],
  templateUrl: './welcome-dash-board.component.html',
  styleUrls: ['./welcome-dash-board.component.css'],
})
export class WelcomeDashboardComponent implements OnInit {
  states: State[] = [];
  selectedState: State | null = null;
  userName: string = 'User';
  showProfileMenu: boolean = false;
  loading: boolean = true;
  errorMessage: string = '';
  products: Product[] = [];
  userProfile: UserProfile | null = null;
  cartCount: number = 0;
  
  constructor(
    private stateService: StateService,
    private userProfileService: UserProfileService,
    private cartService:CartService,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object,
    private elementRef: ElementRef
  ) {}

 ngOnInit(): void {

  if (isPlatformBrowser(this.platformId)) {
    const storedName = localStorage.getItem('userName');
    if (storedName) this.userName = storedName;
  }

  // ✅ Always refresh cart count on navigation
  this.router.events
    .pipe(filter(event => event instanceof NavigationEnd))
    .subscribe(() => {
      this.cartService.syncCartCount();
    });

  // ✅ Subscribe to count
  this.cartService.cartCount$.subscribe(count => {
    this.cartCount = count;
  });

  this.loadStatesFromApi();
  this.loadUserProfile();
}


  loadStatesFromApi(): void {
    this.loading = true;
    this.stateService.getStates().subscribe({
      next: (data) => {
        this.states = data;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = 'Failed to load states from server.';
        console.error(err);
        this.loading = false;
      },
    });
  }

  loadUserProfile(): void {
    this.userProfileService.getUserProfile().subscribe({
      next: (profile) => {
        this.userProfile = profile;
        this.userName = profile.name;
      },
      error: (err) => {
        console.error('Failed to load user profile', err);
      },
    });
  }

  selectState(state: State): void {
    this.selectedState = state;

    this.stateService.getProductsByState(state.stateName).subscribe({
      next: (products) => {
        this.products = products;
        this.errorMessage = products.length === 0 ? `` : '';
      },
      error: (err) => {
        console.error('Failed to load products', err);
        this.products = [];
        this.errorMessage = 'Failed to load products from server.';
      },
    });
  }

  closeDetail(): void {
    this.selectedState = null;
  }

  toggleProfileMenu(): void {
    this.showProfileMenu = !this.showProfileMenu;
  }

  closeProfileMenu(): void {
    setTimeout(() => {
      this.showProfileMenu = false;
    }, 200);
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    if (!this.elementRef.nativeElement.contains(event.target)) {
      this.showProfileMenu = false;
    }
  }

  viewProfile(): void {
    this.closeProfileMenu();
    this.router.navigate(['/viewprofile']);
  }

  Orders():void{
    this.closeProfileMenu();
    this.router.navigate(['/orders']);
  }

  settings(): void {
    this.closeProfileMenu();
    this.router.navigate(['/settings']);
  }

  logout(): void {
    localStorage.clear();
    sessionStorage.clear();
    this.router.navigate(['/login']);
    console.log('User logged out');
  }



addToCart(product: Product): void {
  this.cartService.addToCart(product.productsId!);
  alert(`${product.productsName} added to cart!`);
}

goToCart(): void {
    this.router.navigate(['/cart']); // you’ll need a Cart page
  }
}