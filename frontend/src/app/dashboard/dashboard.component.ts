import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RouterOutlet, RouterLink } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { MenuModule } from 'primeng/menu';
import { FormsModule } from '@angular/forms';
import { MenuItem } from 'primeng/api';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterOutlet, RouterLink, ButtonModule, MenuModule, FormsModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {
  sidebarOpen: boolean = false;
  apiMenuItems: MenuItem[] = [];

  constructor(
    private router: Router,
    private authService: AuthService
  ) {
    this.initApiMenuItems();
  }

  private initApiMenuItems(): void {
    this.apiMenuItems = [
      {
        label: 'Authentication API',
        icon: 'pi pi-shield',
        command: () => {
          window.open('http://localhost:8081/swagger-ui/index.html', '_blank');
        }
      },
      {
        label: 'Management API',
        icon: 'pi pi-users',
        command: () => {
          window.open('http://localhost:8082/swagger-ui/index.html', '_blank');
        }
      },
      {
        label: 'Billing API',
        icon: 'pi pi-file',
        command: () => {
          window.open('http://localhost:8083/swagger-ui/index.html', '_blank');
        }
      },
      {
        label: 'AI API',
        icon: 'pi pi-brain',
        command: () => {
          window.open('http://localhost:8084/docs', '_blank');
        }
      },
      {
        separator: true
      },
      {
        label: 'Postman Collection',
        icon: 'pi pi-download',
        command: () => {
          const link = document.createElement('a');
          link.href = '/assets/docs/POSTMAN_COLLECTION.json';
          link.download = 'MicroBill_API_Collection.json';
          link.click();
        }
      }
    ];
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
  toggleSidebar(): void {
    this.sidebarOpen = !this.sidebarOpen;
    const sidebar = document.querySelector('.sidebar');
    if (sidebar) {
      sidebar.classList.toggle('open', this.sidebarOpen);
    }
  }
}
