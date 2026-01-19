// app/app.routes.ts
import { Routes } from '@angular/router';
import { SignupComponent } from './signup-page/signup-page.component';
import { LoginComponent } from './login-page/login-page.component';
import { WelcomeDashboardComponent } from './welcome-dash-board/welcome-dash-board.component';
import { ViewprofileComponent } from './viewprofile/viewprofile.component';
import { CartComponent } from './cart/cart.component';
import { OrdersComponent } from './orders/orders.component';
import { CheckoutComponent } from './checkout/checkout.component';
import { SupplierProductsComponent } from './supplier-dashboard/supplier-dashboard.component';
import { AdminSupplierDashboardComponent } from './admindashboard/admindashboard.component';
//import { CheckoutComponent } from './checkout/checkout.component';

export const routes: Routes = [
  { path: '', redirectTo: 'signup', pathMatch: 'full' },
  { path: 'signup', component: SignupComponent },
  { path: 'login', component: LoginComponent },
  { path: 'welcome-dash-board', component: WelcomeDashboardComponent },
  { path: 'viewprofile', component: ViewprofileComponent},
  { path: 'cart', component: CartComponent },
  {path:'orders',component:OrdersComponent},
    { path: 'checkout', component:CheckoutComponent },
    {path:'supplier-products',component:SupplierProductsComponent },
    {path:'admindashboard', component:AdminSupplierDashboardComponent}
];
