import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { MessageService } from 'primeng/api';
import { ErrorResponse } from '../shared/models/error-response';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  
  constructor(
    private router: Router,
    private authService: AuthService,
    private messageService: MessageService
  ) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.authService.getToken();

    const publicEndpoints = [
      '/auth/login',
      '/auth/register',
      '/health',
      '/actuator'
    ];

    const isPublic = publicEndpoints.some(endpoint => req.url.includes(endpoint));

    let headers: { [key: string]: string } = {};
    
    // Siempre agregar Content-Type para requests que no sean GET
    if (req.method !== 'GET' && !req.headers.has('Content-Type')) {
      headers['Content-Type'] = 'application/json';
    }
    
    // Agregar Authorization header si hay token y no es endpoint público
    if (token && !isPublic) {
      headers['Authorization'] = `Bearer ${token}`;
      console.log('Adding Authorization header:', `Bearer ${token.substring(0, 20)}...`);
    }
    
    const clonedReq = Object.keys(headers).length > 0 
      ? req.clone({ setHeaders: headers })
      : req;
      
    console.log('Request URL:', req.url, 'Headers:', clonedReq.headers.keys());

    return next.handle(clonedReq).pipe(
      catchError((error: HttpErrorResponse) => {
        const errorResponse: ErrorResponse = error.error;
        
        if (error.status === 401 && !isPublic) {
          this.authService.logout();
          this.messageService.add({
            severity: 'warn',
            summary: 'Sesión expirada',
            detail: 'Por favor, inicie sesión nuevamente'
          });
          this.router.navigate(['/login']);
        } else if (error.status >= 500) {
          this.messageService.add({
            severity: 'error',
            summary: 'Error del servidor',
            detail: errorResponse?.message || 'Error interno del servidor'
          });
        }
        
        return throwError(() => error);
      })
    );
  }
}
