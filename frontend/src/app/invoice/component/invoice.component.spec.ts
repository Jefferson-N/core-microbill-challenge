import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MessageService } from 'primeng/api';
import { InvoiceComponent } from './invoice.component';
import { BillingService } from '../../core/services/billing.service';
import { of } from 'rxjs';

describe('InvoiceComponent', () => {
  let component: InvoiceComponent;
  let fixture: ComponentFixture<InvoiceComponent>;
  let billingService: any;

  beforeEach(async () => {
    const billingServiceSpy = {
      getInvoices: jasmine.createSpy().and.returnValue(of({ content: [], totalElements: 0, totalPages: 0, size: 10, number: 0 })),
      deleteInvoice: jasmine.createSpy().and.returnValue(of(null))
    };

    await TestBed.configureTestingModule({
      imports: [InvoiceComponent, HttpClientTestingModule],
      providers: [
        MessageService,
        { provide: BillingService, useValue: billingServiceSpy }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InvoiceComponent);
    component = fixture.componentInstance;
    billingService = TestBed.inject(BillingService);
    
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load invoices on init', () => {
    expect(billingService.getInvoices).toHaveBeenCalled();
  });
});
