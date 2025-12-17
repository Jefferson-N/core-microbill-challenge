import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/enviroment';

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export interface Customer {
  id?: number;
  name: string;
  docNumber: string;
  email: string;
  address?: string;
}

export interface Provider {
  id?: number;
  name: string;
  taxId: string;
  email: string;
  address?: string;
}

export interface Product {
  id?: number;
  code: string;
  name: string;
  price: number;
  taxRate: number;
  stock: number;
}

@Injectable({ providedIn: 'root' })
export class ManagementService {

  private managementUrl = environment.services.management;

  constructor(private http: HttpClient) { }

  // Customer methods
  getCustomers(page = 0, size = 20, search?: string): Observable<PageResponse<Customer>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    if (search) {
      params = params.set('search', search);
    }

    return this.http.get<PageResponse<Customer>>(`${this.managementUrl}/customers`, { params });
  }

  getCustomer(id: number): Observable<Customer> {
    return this.http.get<Customer>(`${this.managementUrl}/customers/${id}`);
  }

  createCustomer(customer: Customer): Observable<Customer> {
    return this.http.post<Customer>(`${this.managementUrl}/customers`, customer);
  }

  updateCustomer(id: number, customer: Customer): Observable<Customer> {
    return this.http.put<Customer>(`${this.managementUrl}/customers/${id}`, customer);
  }

  deleteCustomer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.managementUrl}/customers/${id}`);
  }

  // Provider methods
  getProviders(page = 0, size = 20): Observable<PageResponse<Provider>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<PageResponse<Provider>>(`${this.managementUrl}/providers`, { params });
  }

  createProvider(provider: Provider): Observable<Provider> {
    return this.http.post<Provider>(`${this.managementUrl}/providers`, provider);
  }

  updateProvider(id: number, provider: Provider): Observable<Provider> {
    return this.http.put<Provider>(`${this.managementUrl}/providers/${id}`, provider);
  }

  deleteProvider(id: number): Observable<void> {
    return this.http.delete<void>(`${this.managementUrl}/providers/${id}`);
  }

  // Product methods
  getProducts(page = 0, size = 20): Observable<PageResponse<Product>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<PageResponse<Product>>(`${this.managementUrl}/products`, { params });
  }

  createProduct(product: Product): Observable<Product> {
    return this.http.post<Product>(`${this.managementUrl}/products`, product);
  }

  updateProduct(id: number, product: Product): Observable<Product> {
    return this.http.put<Product>(`${this.managementUrl}/products/${id}`, product);
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.managementUrl}/products/${id}`);
  }
}