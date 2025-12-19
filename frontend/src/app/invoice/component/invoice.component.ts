import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { BillingService, Invoice } from '../../core/services/billing.service';
import { ManagementService, Customer, Provider } from '../../core/services/management.service';
import { Router } from '@angular/router';
import { AuthService } from '../../auth/auth.service';

@Component({
  selector: 'app-invoice',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    TableModule,
    ButtonModule,
    ToastModule
  ],
  templateUrl: './invoice.component.html',
  styleUrls: ['./invoice.component.scss'],
  providers: [MessageService]
})
export class InvoiceComponent implements OnInit {
  invoices: Invoice[] = [];
  customers: Customer[] = [];
  providers: Provider[] = [];
  loading = false;


  constructor(
    private billingService: BillingService,
    private managementService: ManagementService,
    private authService: AuthService,
    private messageService: MessageService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    if (!this.authService.isAuthenticated()) {
      this.authService.logout();
      this.router.navigate(['/login']);
      return;
    }
    this.loadData();
  }

  loadData(): void {
    this.loadInvoices();
    this.loadCustomers();
    this.loadProviders();
  }

  loadCustomers(): void {
    this.managementService.getCustomers(0, 1000).subscribe({
      next: (response) => {
        this.customers = response.content;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error loading customers:', err);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: err.error?.message || 'Could not load customers'
        });
      }
    });
  }

  loadProviders(): void {
    this.managementService.getProviders(0, 1000).subscribe({
      next: (response) => {
        this.providers = response.content;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error loading providers:', err);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: err.error?.message || 'Could not load providers'
        });
      }
    });
  }

  loadInvoices(): void {
    this.loading = true;
    this.cdr.detectChanges();
    
    this.billingService.getInvoices(0, 20).subscribe({
      next: (response) => {
        this.invoices = response.content;
        setTimeout(() => {
          this.loading = false;
          this.cdr.detectChanges();
        });
      },
      error: (err) => {
        console.error('Error loading invoices:', err);
        setTimeout(() => {
          this.loading = false;
          this.cdr.detectChanges();
        });
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: err.error?.message || 'Could not load invoices'
        });
      }
    });
  }

  getCustomerName(customerId: number): string {
    const customer = this.customers.find(c => c.id === customerId);
    return customer?.name || 'N/A';
  }

  getProviderName(providerId: number): string {
    const provider = this.providers.find(p => p.id === providerId);
    return provider?.name || 'N/A';
  }
}
