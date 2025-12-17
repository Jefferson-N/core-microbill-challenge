import { ChangeDetectorRef, Component, EventEmitter, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { TableModule } from 'primeng/table';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { PaginatorModule } from 'primeng/paginator';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { MessageService, ConfirmationService } from 'primeng/api';
import { ManagementService, Customer, PageResponse } from '../../core/services/management.service';

@Component({
  selector: 'app-clients',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TableModule,
    DialogModule,
    InputGroupAddonModule,
    InputGroupModule,
    ButtonModule,
    InputTextModule,
    PaginatorModule,
    ToastModule,
    ConfirmDialogModule
  ],
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.scss'],
  providers: [MessageService, ConfirmationService]
})
export class ClientsComponent implements OnInit {
  
  customers: Customer[] = [];
  customer: Customer = { name: '', docNumber: '', email: '' };
  customerDialog: boolean = false;
  submitted: boolean = false;
  searchValue: string = '';
  
  // Pagination
  totalRecords: number = 0;
  rows: number = 10;
  first: number = 0;

  @Output() clientSelected = new EventEmitter<Customer>();

  constructor(
    private managementService: ManagementService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadCustomers();
  }

  loadCustomers(): void {
    const page = Math.floor(this.first / this.rows);
    this.managementService.getCustomers(page, this.rows, this.searchValue).subscribe({
      next: (response: PageResponse<Customer>) => {
        this.customers = response.content;
        this.totalRecords = response.totalElements;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error loading customers:', err);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'No se pudieron cargar los clientes'
        });
      }
    });
  }

  onPageChange(event: any): void {
    this.first = event.first;
    this.rows = event.rows;
    this.loadCustomers();
  }

  onSearch(): void {
    this.first = 0;
    this.loadCustomers();
  }

  onSelectClient(customer: Customer): void {
    this.clientSelected.emit(customer);
  }

  openNew(): void {
    this.customer = { name: '', docNumber: '', email: '' };
    this.submitted = false;
    this.customerDialog = true;
  }

  editCustomer(customer: Customer): void {
    this.customer = { ...customer };
    this.customerDialog = true;
  }

  deleteCustomer(customer: Customer): void {
    this.confirmationService.confirm({
      message: `¿Está seguro de que desea eliminar a ${customer.name}?`,
      header: 'Confirmar',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.managementService.deleteCustomer(customer.id!).subscribe({
          next: () => {
            this.loadCustomers();
            this.messageService.add({
              severity: 'success',
              summary: 'Exitoso',
              detail: 'Cliente eliminado'
            });
          },
          error: (err) => {
            console.error('Error deleting customer:', err);
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'No se pudo eliminar el cliente'
            });
          }
        });
      }
    });
  }

  hideDialog(): void {
    this.customerDialog = false;
    this.submitted = false;
  }

  saveCustomer(): void {
    this.submitted = true;

    if (this.customer.name?.trim() && this.customer.docNumber?.trim() && this.customer.email?.trim()) {
      if (this.customer.id) {
        this.managementService.updateCustomer(this.customer.id, this.customer).subscribe({
          next: () => {
            this.loadCustomers();
            this.messageService.add({
              severity: 'success',
              summary: 'Exitoso',
              detail: 'Cliente actualizado'
            });
            this.hideDialog();
          },
          error: (err) => {
            console.error('Error updating customer:', err);
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'No se pudo actualizar el cliente'
            });
          }
        });
      } else {
        this.managementService.createCustomer(this.customer).subscribe({
          next: () => {
            this.loadCustomers();
            this.messageService.add({
              severity: 'success',
              summary: 'Exitoso',
              detail: 'Cliente creado'
            });
            this.hideDialog();
          },
          error: (err) => {
            console.error('Error creating customer:', err);
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'No se pudo crear el cliente'
            });
          }
        });
      }
    }
  }
}