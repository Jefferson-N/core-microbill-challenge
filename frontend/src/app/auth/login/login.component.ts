import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';
import { AuthService } from '../auth.service';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { DialogModule } from 'primeng/dialog';
import { ErrorResponse } from '../../shared/models/error-response';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    InputTextModule,
    PasswordModule,
    ButtonModule,
    ToastModule,
    DialogModule   
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  providers: [MessageService] 
})
export class LoginComponent {

  username: string = '';
  password: string = '';

  registerDialogVisible = false;
  regUsername: string = '';
  regEmail: string = '';
  regPassword: string = '';

  constructor(
    private authService: AuthService,
    private router: Router,
    private messageService: MessageService
  ) { }

  onSubmit(): void {
    this.authService.login({ username: this.username, password: this.password })
      .subscribe({
        next: (response) => {
          this.messageService.add({
            severity: 'success',
            summary: 'Login exitoso',
            detail: `Bienvenido ${response.username}`
          });
          this.router.navigate(['/customers']);
        },
        error: (err) => {
          console.error('Error en login:', err);
          const errorResponse: ErrorResponse = err.error;
          let errorMessage = 'Usuario o contraseña incorrectos';
          let errorSummary = 'Error de login';
          
          if (errorResponse) {
            errorMessage = errorResponse.message || errorMessage;
            errorSummary = errorResponse.error || errorSummary;
          }
          
          this.messageService.add({
            severity: 'error',
            summary: errorSummary,
            detail: errorMessage
          });
        }
      });
  }

  onRegister(): void {
    this.registerDialogVisible = false;
  }
}
