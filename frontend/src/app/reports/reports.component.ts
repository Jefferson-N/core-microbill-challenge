import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { DialogModule } from 'primeng/dialog';
import { InputNumberModule } from 'primeng/inputnumber';
import { TooltipModule } from 'primeng/tooltip';
import { MessageService } from 'primeng/api';
import { BillingService, Invoice } from '../core/services/billing.service';
import { ManagementService, Product } from '../core/services/management.service';
import { Observable } from 'rxjs';

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
    TooltipModule
  ],
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.scss'],
  providers: [MessageService]
})
export class ReportsComponent implements OnInit {
  invoices: Invoice[] = [];
  loading = false;
  
  // View/Edit dialog
  displayDialog = false;
  selectedInvoice: Invoice | null = null;
  editForm: FormGroup;
  products: Product[] = [];
  isEditing = false;

  constructor(
    private billingService: BillingService,
    private managementService: ManagementService,
    private messageService: MessageService,
    private cdr: ChangeDetectorRef,
    private fb: FormBuilder
  ) {
    this.editForm = this.fb.group({
      items: this.fb.array([])
    });
  }

  ngOnInit(): void {
    this.loadInvoices();
    this.loadProducts();
  }

  get itemsFormArray(): FormArray {
    return this.editForm.get('items') as FormArray;
  }

  loadProducts(): void {
    this.managementService.getProducts(0, 100).subscribe(response => {
      this.products = response.content;
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
    this.selectedInvoice = invoice;
    this.isEditing = true;
    this.setupEditForm(invoice);
    this.displayDialog = true;
  }

  setupEditForm(invoice: Invoice): void {
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
