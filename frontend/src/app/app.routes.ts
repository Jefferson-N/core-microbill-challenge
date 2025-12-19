import { Routes } from '@angular/router';
import { AuthGuard } from './core/auth.guard';
import { DashboardComponent } from './dashboard/dashboard.component';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./auth/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: '',
    component: DashboardComponent,
    canActivate: [AuthGuard],
    canActivateChild: [AuthGuard],
    children: [
      {
        path: 'customers',
        loadComponent: () => import('./clients/component/clients.component').then(m => m.ClientsComponent)
      },
      {
        path: 'providers',
        loadComponent: () => import('./suppliers/component/suppliers.component').then(m => m.SuppliersComponent)
      },
      {
        path: 'products',
        loadComponent: () => import('./products/component/products.component').then(m => m.ProductsComponent)
      },
      {
        path: 'invoices',
        loadComponent: () => import('./invoice/component/invoice.component').then(m => m.InvoiceComponent)
      },
      {
        path: 'invoice-form',
        loadComponent: () => import('./invoice/component/invoice-form.component').then(m => m.InvoiceFormComponent)
      },
      {
        path: 'reports',
        loadComponent: () => import('./reports/reports.component').then(m => m.ReportsComponent)
      },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },
  { path: '**', redirectTo: '/login' }
];
