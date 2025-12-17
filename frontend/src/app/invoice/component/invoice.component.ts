import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { BillingService, Invoice } from '../../core/services/billing.service';

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
  loading = false;

  constructor(
    private billingService: BillingService,
    private messageService: MessageService
  ) { }

  ngOnInit(): void {
    this.loadInvoices();
  }

  loadInvoices(): void {
    this.loading = true;
    this.billingService.getInvoices(0, 20).subscribe({
      next: (response) => {
        this.invoices = response.content;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading invoices:', err);
        this.loading = false;
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'No se pudieron cargar las facturas'
        });
      }
    });
  }

  deleteInvoice(id: number): void {
    this.billingService.deleteInvoice(id).subscribe({
      next: () => {
        this.loadInvoices();
        this.messageService.add({
          severity: 'success',
          summary: 'Éxito',
          detail: 'Factura eliminada correctamente'
        });
      },
      error: (err) => {
        console.error('Error deleting invoice:', err);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'No se pudo eliminar la factura'
        });
      }
    });
  }
}
