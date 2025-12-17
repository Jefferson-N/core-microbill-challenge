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
          this.router.navigate(['/dashboard']);
        },
        error: (err) => {
          console.error('Error en login:', err);
          this.messageService.add({
            severity: 'error',
            summary: 'Error de login',
            detail: 'Usuario o contraseña incorrectos'
          });
        }
      });
  }

  onRegister(): void {
    // Registro deshabilitado - usar usuarios demo
    this.messageService.add({
      severity: 'info',
      summary: 'Información',
      detail: 'Use usuarios demo: admin@demo.com/Admin#123 o user@demo.com/User#123'
    });
    this.registerDialogVisible = false;
  }
}
