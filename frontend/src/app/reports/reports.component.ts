import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { DialogModule } from 'primeng/dialog';
import { InputNumberModule } from 'primeng/inputnumber';
import { TooltipModule } from 'primeng/tooltip';
import { MessageService, ConfirmationService } from 'primeng/api';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { BillingService, Invoice } from '../core/services/billing.service';
import { ManagementService, Product, Customer, Provider } from '../core/services/management.service';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    TableModule,
    ButtonModule,
    ToastModule,
    DialogModule,
    InputNumberModule,
    TooltipModule,
    ConfirmDialogModule
  ],
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.scss'],
  providers: [MessageService, ConfirmationService]
})
export class ReportsComponent implements OnInit {
  invoices: Invoice[] = [];
  customers: Customer[] = [];
  providers: Provider[] = [];
  products: Product[] = [];
  loading = false;
  
  displayDialog = false;
  selectedInvoice: Invoice | null = null;
  editForm: FormGroup;
  isEditing = false;

  constructor(
    private billingService: BillingService,
    private managementService: ManagementService,
    private authService: AuthService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private fb: FormBuilder
  ) {
    this.editForm = this.fb.group({
      customerId: ['', Validators.required],
      providerId: ['', Validators.required],
      items: this.fb.array([])
    });
  }

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
    this.loadProducts();
  }

  loadCustomers(): void {
    this.managementService.getCustomers(0, 1000).subscribe({
      next: (response) => {
        this.customers = response.content;
        this.cdr.detectChanges();
      },
      error: (err) => {
        if (err.status === 401) {
          this.authService.logout();
          this.router.navigate(['/login']);
        }
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
        if (err.status === 401) {
          this.authService.logout();
          this.router.navigate(['/login']);
        }
      }
    });
  }

  get itemsFormArray(): FormArray {
    return this.editForm.get('items') as FormArray;
  }

  loadProducts(): void {
    this.managementService.getProducts(0, 1000).subscribe({
      next: (response) => {
        this.products = response.content;
        this.cdr.detectChanges();
      },
      error: (err) => {
        if (err.status === 401) {
          this.authService.logout();
          this.router.navigate(['/login']);
        }
      }
    });
  }

  loadInvoices(): void {
    this.loading = true;
    this.cdr.detectChanges();
    
    this.billingService.getInvoices(0, 50).subscribe({
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

  viewInvoice(invoice: Invoice): void {
    this.selectedInvoice = invoice;
    this.isEditing = false;
    this.displayDialog = true;
  }

  editInvoice(invoice: Invoice): void {
    if (!this.customers.length || !this.providers.length) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Advertencia',
        detail: 'No se puede editar: faltan datos de clientes o proveedores'
      });
      return;
    }
    this.selectedInvoice = invoice;
    this.isEditing = true;
    this.setupEditForm(invoice);
    this.displayDialog = true;
  }

  setupEditForm(invoice: Invoice): void {
    this.editForm.patchValue({
      customerId: invoice.customerId,
      providerId: invoice.providerId
    });
    this.itemsFormArray.clear();
    invoice.items?.forEach(item => {
      const itemForm = this.fb.group({
        id: [item.id],
        productId: [item.productId, Validators.required],
        quantity: [item.quantity, [Validators.required, Validators.min(1)]],
        unitPrice: [item.unitPrice, [Validators.required, Validators.min(0)]],
        taxRate: [item.taxRate || 0.19]
      });
      this.itemsFormArray.push(itemForm);
    });
  }

  addItem(): void {
    const itemForm = this.fb.group({
      id: [null],
      productId: ['', Validators.required],
      quantity: [1, [Validators.required, Validators.min(1)]],
      unitPrice: [0, [Validators.required, Validators.min(0)]],
      taxRate: [0.19]
    });
    this.itemsFormArray.push(itemForm);
  }

  removeItem(index: number): void {
    this.itemsFormArray.removeAt(index);
  }

  onProductChange(index: number, event: any): void {
    const productId = event.target ? event.target.value : event.value;
    const product = this.products.find(p => p.id == productId);
    if (product) {
      const itemForm = this.itemsFormArray.at(index);
      itemForm.patchValue({
        unitPrice: product.price,
        taxRate: product.taxRate
      });
    }
  }

  getItemSubtotal(index: number): number {
    const item = this.itemsFormArray.at(index)?.value;
    return (item?.quantity || 0) * (item?.unitPrice || 0);
  }

  saveChanges(): void {
    if (this.editForm.valid && this.selectedInvoice) {
      const updatedInvoice = {
        ...this.selectedInvoice,
        items: this.itemsFormArray.value
      };

      this.billingService.updateInvoice(this.selectedInvoice.id!, updatedInvoice).subscribe({
        next: () => {
          this.messageService.add({
            severity: 'success',
            summary: 'Éxito',
            detail: 'Factura actualizada correctamente'
          });
          this.displayDialog = false;
          this.loadInvoices();
        },
        error: (err) => {
          console.error('Error updating invoice:', err);
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'No se pudo actualizar la factura'
          });
        }
      });
    }
  }

  deleteInvoice(id: number): void {
    this.confirmationService.confirm({
      message: '¿Está seguro de que desea eliminar esta factura?',
      header: 'Confirmar Eliminación',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
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
