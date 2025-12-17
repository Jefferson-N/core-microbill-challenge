import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { TableModule } from 'primeng/table';
import { CardModule } from 'primeng/card';
import { PanelModule } from 'primeng/panel';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { BillingService, Invoice, InvoiceItem, RecommendationResponse, AnomalyResponse } from '../../core/services/billing.service';
import { ManagementService, Customer, Provider, Product } from '../../core/services/management.service';

@Component({
  selector: 'app-invoice-form',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    ButtonModule,
    InputTextModule,
    InputNumberModule,
    TableModule,
    CardModule,
    PanelModule,
    ToastModule
  ],
  templateUrl: './invoice-form.component.html',
  styleUrls: ['./invoice-form.component.scss'],
  providers: [MessageService]
})
export class InvoiceFormComponent implements OnInit {

  invoiceForm: FormGroup;
  customers: Customer[] = [];
  providers: Provider[] = [];
  products: Product[] = [];
  
  recommendations: RecommendationResponse | null = null;
  anomalyResult: AnomalyResponse | null = null;
  
  invoiceTotals = { subtotal: 0, taxTotal: 0, total: 0 };

  constructor(
    private fb: FormBuilder,
    private billingService: BillingService,
    private managementService: ManagementService,
    private messageService: MessageService
  ) {
    this.invoiceForm = this.fb.group({
      customerId: ['', Validators.required],
      providerId: ['', Validators.required],
      items: this.fb.array([])
    });
  }

  ngOnInit(): void {
    this.loadData();
    this.addItem();
  }

  get itemsFormArray(): FormArray {
    return this.invoiceForm.get('items') as FormArray;
  }

  loadData(): void {
    this.managementService.getCustomers(0, 100).subscribe(response => {
      this.customers = response.content;
    });

    this.managementService.getProviders(0, 100).subscribe(response => {
      this.providers = response.content;
    });

    this.managementService.getProducts(0, 100).subscribe(response => {
      this.products = response.content;
    });
  }

  addItem(): void {
    const itemForm = this.fb.group({
      productId: ['', Validators.required],
      quantity: [1, [Validators.required, Validators.min(1)]],
      unitPrice: [0, [Validators.required, Validators.min(0)]],
      taxRate: [0.19]
    });

    this.itemsFormArray.push(itemForm);
  }

  removeItem(index: number): void {
    this.itemsFormArray.removeAt(index);
    this.calculateTotals();
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
      this.calculateItemTotal(index);
    }
  }

  calculateItemTotal(index: number): void {
    setTimeout(() => this.calculateTotals(), 0);
  }

  calculateTotals(): void {
    const items = this.itemsFormArray.value.map((item: any) => ({
      ...item,
      taxRate: item.taxRate || 0.19
    }));
    
    this.invoiceTotals = this.billingService.calculateInvoiceTotals(items);
  }

  getItemSubtotal(index: number): number {
    const item = this.itemsFormArray.at(index)?.value;
    return (item?.quantity || 0) * (item?.unitPrice || 0);
  }

  getItemTax(index: number): number {
    const subtotal = this.getItemSubtotal(index);
    const item = this.itemsFormArray.at(index)?.value;
    return subtotal * (item?.taxRate || 0.19);
  }

  getItemTotal(index: number): number {
    return this.getItemSubtotal(index) + this.getItemTax(index);
  }

  getRecommendations(): void {
    const customerId = this.invoiceForm.get('customerId')?.value;
    if (customerId) {
      this.billingService.getRecommendations(customerId).subscribe({
        next: (response) => {
          this.recommendations = response;
        },
        error: (err) => {
          console.error('Error getting recommendations:', err);
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'No se pudieron obtener las recomendaciones'
          });
        }
      });
    }
  }

  addRecommendedProduct(product: Product): void {
    const existingIndex = this.itemsFormArray.controls.findIndex(
      control => control.get('productId')?.value == product.id
    );

    if (existingIndex >= 0) {
      const existingItem = this.itemsFormArray.at(existingIndex);
      const currentQuantity = existingItem.get('quantity')?.value || 0;
      existingItem.patchValue({ quantity: currentQuantity + 1 });
      this.calculateItemTotal(existingIndex);
    } else {
      this.addItem();
      const newIndex = this.itemsFormArray.length - 1;
      this.itemsFormArray.at(newIndex).patchValue({
        productId: product.id,
        quantity: 1,
        unitPrice: product.price,
        taxRate: product.taxRate
      });
      this.calculateItemTotal(newIndex);
    }
  }

  detectAnomalies(): void {
    const customerId = this.invoiceForm.get('customerId')?.value;
    const items = this.itemsFormArray.value;
    
    if (customerId && items.length > 0) {
      this.billingService.detectAnomalies(customerId, this.invoiceTotals.total, items).subscribe({
        next: (response) => {
          this.anomalyResult = response;
        },
        error: (err) => {
          console.error('Error detecting anomalies:', err);
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'No se pudo detectar anomalías'
          });
        }
      });
    }
  }

  onSubmit(): void {
    if (this.invoiceForm.valid && this.itemsFormArray.length > 0) {
      const invoice: Invoice = {
        customerId: this.invoiceForm.get('customerId')?.value,
        providerId: this.invoiceForm.get('providerId')?.value,
        items: this.itemsFormArray.value,
        subtotal: this.invoiceTotals.subtotal,
        taxTotal: this.invoiceTotals.taxTotal,
        total: this.invoiceTotals.total
      };

      this.billingService.createInvoice(invoice).subscribe({
        next: (response) => {
          this.messageService.add({
            severity: 'success',
            summary: 'Éxito',
            detail: 'Factura creada correctamente'
          });
          this.resetForm();
        },
        error: (err) => {
          console.error('Error creating invoice:', err);
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'No se pudo crear la factura'
          });
        }
      });
    }
  }

  resetForm(): void {
    this.invoiceForm.reset();
    this.itemsFormArray.clear();
    this.addItem();
    this.recommendations = null;
    this.anomalyResult = null;
    this.invoiceTotals = { subtotal: 0, taxTotal: 0, total: 0 };
  }
}