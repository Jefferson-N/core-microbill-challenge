import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { BillingService, Invoice } from '../core/services/billing.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [
    CommonModule,
    TableModule,
    ButtonModule,
    ToastModule
  ],
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.scss'],
  providers: [MessageService]
})
export class ReportsComponent implements OnInit {
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
    this.billingService.getInvoices(0, 50).subscribe({
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

  downloadPdf(id: number): void {
    this.billingService.generateReport(id).subscribe({
      next: (pdfBlob) => {
        const url = window.URL.createObjectURL(pdfBlob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `factura-${id}.pdf`;
        a.click();
        window.URL.revokeObjectURL(url);
        this.messageService.add({
          severity: 'success',
          summary: 'Éxito',
          detail: 'PDF descargado correctamente'
        });
      },
      error: (err) => {
        console.error('Error downloading PDF:', err);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'No se pudo descargar el PDF'
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
