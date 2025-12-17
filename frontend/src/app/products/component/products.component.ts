import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { TableModule } from 'primeng/table';
import { PaginatorModule } from 'primeng/paginator';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { MessageService, ConfirmationService } from 'primeng/api';
import { ManagementService, Product, PageResponse } from '../../core/services/management.service';

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TableModule,
    DialogModule,
    ButtonModule,
    InputTextModule,
    InputNumberModule,
    PaginatorModule,
    ToastModule,
    ConfirmDialogModule
  ],
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss'],
  providers: [MessageService, ConfirmationService]
})
export class ProductsComponent implements OnInit {
  
  products: Product[] = [];
  product: Product = { code: '', name: '', price: 0, taxRate: 0.19, stock: 0 };
  productDialog: boolean = false;
  submitted: boolean = false;
  
  // Pagination
  totalRecords: number = 0;
  rows: number = 10;
  first: number = 0;

  constructor(
    private managementService: ManagementService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    const page = Math.floor(this.first / this.rows);
    this.managementService.getProducts(page, this.rows).subscribe({
      next: (response: PageResponse<Product>) => {
        this.products = response.content;
        this.totalRecords = response.totalElements;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error loading products:', err);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'No se pudieron cargar los productos'
        });
      }
    });
  }

  onPageChange(event: any): void {
    this.first = event.first;
    this.rows = event.rows;
    this.loadProducts();
  }

  openNew(): void {
    this.product = { code: '', name: '', price: 0, taxRate: 0.19, stock: 0 };
    this.submitted = false;
    this.productDialog = true;
  }

  editProduct(product: Product): void {
    this.product = { ...product };
    this.productDialog = true;
  }

  deleteProduct(product: Product): void {
    this.confirmationService.confirm({
      message: `¿Está seguro de que desea eliminar ${product.name}?`,
      header: 'Confirmar',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.managementService.deleteProduct(product.id!).subscribe({
          next: () => {
            this.loadProducts();
            this.messageService.add({
              severity: 'success',
              summary: 'Exitoso',
              detail: 'Producto eliminado'
            });
          },
          error: (err) => {
            console.error('Error deleting product:', err);
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'No se pudo eliminar el producto'
            });
          }
        });
      }
    });
  }

  hideDialog(): void {
    this.productDialog = false;
    this.submitted = false;
  }

  saveProduct(): void {
    this.submitted = true;

    if (this.product.code?.trim() && this.product.name?.trim() && this.product.price >= 0) {
      if (this.product.id) {
        this.managementService.updateProduct(this.product.id, this.product).subscribe({
          next: () => {
            this.loadProducts();
            this.messageService.add({
              severity: 'success',
              summary: 'Exitoso',
              detail: 'Producto actualizado'
            });
            this.hideDialog();
          },
          error: (err) => {
            console.error('Error updating product:', err);
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'No se pudo actualizar el producto'
            });
          }
        });
      } else {
        this.managementService.createProduct(this.product).subscribe({
          next: () => {
            this.loadProducts();
            this.messageService.add({
              severity: 'success',
              summary: 'Exitoso',
              detail: 'Producto creado'
            });
            this.hideDialog();
          },
          error: (err) => {
            console.error('Error creating product:', err);
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'No se pudo crear el producto'
            });
          }
        });
      }
    }
  }
}