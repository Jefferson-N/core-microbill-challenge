import { Component, OnInit, ChangeDetectorRef, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { TableModule } from 'primeng/table';
import { PaginatorModule } from 'primeng/paginator';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { MessageService, ConfirmationService } from 'primeng/api';
import { ManagementService, Provider, PageResponse } from '../../core/services/management.service';

@Component({
  selector: 'app-suppliers',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TableModule,
    DialogModule,
    ButtonModule,
    InputTextModule,
    PaginatorModule,
    ToastModule,
    ConfirmDialogModule
  ],
  templateUrl: './suppliers.component.html',
  styleUrls: ['./suppliers.component.scss'],
  providers: [MessageService, ConfirmationService]
})
export class SuppliersComponent implements OnInit {
  
  providers: Provider[] = [];
  provider: Provider = { name: '', taxId: '', email: '' };
  providerDialog: boolean = false;
  submitted: boolean = false;
  searchValue: string = '';
  
  // Pagination
  totalRecords: number = 0;
  rows: number = 10;
  first: number = 0;

  @Output() providerSelected = new EventEmitter<Provider>();

  constructor(
    private managementService: ManagementService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadProviders();
  }

  loadProviders(): void {
    const page = Math.floor(this.first / this.rows);
    this.managementService.getProviders(page, this.rows).subscribe({
      next: (response: PageResponse<Provider>) => {
        this.providers = response.content;
        this.totalRecords = response.totalElements;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error loading providers:', err);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'No se pudieron cargar los proveedores'
        });
      }
    });
  }

  onPageChange(event: any): void {
    this.first = event.first;
    this.rows = event.rows;
    this.loadProviders();
  }

  onSelectProvider(provider: Provider): void {
    this.providerSelected.emit(provider);
  }

  openNew(): void {
    this.provider = { name: '', taxId: '', email: '' };
    this.submitted = false;
    this.providerDialog = true;
  }

  editProvider(provider: Provider): void {
    this.provider = { ...provider };
    this.providerDialog = true;
  }

  deleteProvider(provider: Provider): void {
    this.confirmationService.confirm({
      message: `¿Está seguro de que desea eliminar a ${provider.name}?`,
      header: 'Confirmar',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.managementService.deleteProvider(provider.id!).subscribe({
          next: () => {
            this.loadProviders();
            this.messageService.add({
              severity: 'success',
              summary: 'Exitoso',
              detail: 'Proveedor eliminado'
            });
          },
          error: (err) => {
            console.error('Error deleting provider:', err);
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'No se pudo eliminar el proveedor'
            });
          }
        });
      }
    });
  }

  hideDialog(): void {
    this.providerDialog = false;
    this.submitted = false;
  }

  saveProvider(): void {
    this.submitted = true;

    if (this.provider.name?.trim() && this.provider.taxId?.trim() && this.provider.email?.trim()) {
      if (this.provider.id) {
        this.managementService.updateProvider(this.provider.id, this.provider).subscribe({
          next: () => {
            this.loadProviders();
            this.messageService.add({
              severity: 'success',
              summary: 'Exitoso',
              detail: 'Proveedor actualizado'
            });
            this.hideDialog();
          },
          error: (err) => {
            console.error('Error updating provider:', err);
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'No se pudo actualizar el proveedor'
            });
          }
        });
      } else {
        this.managementService.createProvider(this.provider).subscribe({
          next: () => {
            this.loadProviders();
            this.messageService.add({
              severity: 'success',
              summary: 'Exitoso',
              detail: 'Proveedor creado'
            });
            this.hideDialog();
          },
          error: (err) => {
            console.error('Error creating provider:', err);
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'No se pudo crear el proveedor'
            });
          }
        });
      }
    }
  }
}