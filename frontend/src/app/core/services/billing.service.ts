import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/enviroment';
import { Customer, Provider, Product, PageResponse } from './management.service';

export interface InvoiceItem {
  id?: number;
  product?: Product;
  productId: number;
  quantity: number;
  unitPrice: number;
  taxRate?: number;
  lineTotal?: number;
}

export interface Invoice {
  id?: number;
  customer?: Customer;
  customerId: number;
  provider?: Provider;
  providerId: number;
  issueDate?: string;
  subtotal?: number;
  taxTotal?: number;
  total?: number;
  status?: 'DRAFT' | 'ISSUED' | 'PAID' | 'CANCELLED';
  items: InvoiceItem[];
}

export interface ProductRecommendation {
  productId: number;
  productName: string;
  price: number;
  score: number;
  reason: string;
}

export interface RecommendationResponse {
  customerId: number;
  products: ProductRecommendation[];
  reason: string;
}

export interface AnomalyResponse {
  score: number;
  explanation: string;
}

@Injectable({ providedIn: 'root' })
export class BillingService {

  private billingUrl = environment.services.billing;
  private aiUrl = environment.services.ai;

  constructor(private http: HttpClient) { }

  // Invoice methods
  getInvoices(page = 0, size = 20): Observable<PageResponse<Invoice>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<PageResponse<Invoice>>(`${this.billingUrl}/invoices`, { params });
  }

  getInvoice(id: number): Observable<Invoice> {
    return this.http.get<Invoice>(`${this.billingUrl}/invoices/${id}`);
  }

  createInvoice(invoice: Invoice): Observable<Invoice> {
    return this.http.post<Invoice>(`${this.billingUrl}/invoices`, invoice);
  }

  updateInvoice(id: number, invoice: Invoice): Observable<Invoice> {
    return this.http.put<Invoice>(`${this.billingUrl}/invoices/${id}`, invoice);
  }

  deleteInvoice(id: number): Observable<void> {
    return this.http.delete<void>(`${this.billingUrl}/invoices/${id}`);
  }

  generateReport(id: number): Observable<Blob> {
    return this.http.post(`${this.billingUrl}/invoices/${id}/generate-report`, null, {
      responseType: 'blob'
    });
  }

  // AI methods
  getRecommendations(customerId: number): Observable<RecommendationResponse> {
    const params = new HttpParams().set('customerId', customerId.toString());
    return this.http.post<RecommendationResponse>(`${this.aiUrl}/ai/recommendations`, null, { params });
  }

  detectAnomalies(customerId: number, total: number, items: InvoiceItem[]): Observable<AnomalyResponse> {
    const request = { customerId, total, items };
    return this.http.post<AnomalyResponse>(`${this.aiUrl}/ai/anomaly-score`, request);
  }

  calculateInvoiceTotals(items: InvoiceItem[]): { subtotal: number; taxTotal: number; total: number } {
    let subtotal = 0;
    let taxTotal = 0;

    items.forEach(item => {
      const itemSubtotal = item.quantity * item.unitPrice;
      const itemTax = itemSubtotal * (item.taxRate || 0);

      subtotal += itemSubtotal;
      taxTotal += itemTax;

      item.lineTotal = itemSubtotal + itemTax;
    });

    return {
      subtotal,
      taxTotal,
      total: subtotal + taxTotal
    };
  }
}

export { type PageResponse };
