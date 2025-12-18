import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, tap } from 'rxjs';
import { environment } from '../../environments/enviroment';
import { UserInfo } from './models/user-info';
import { TokenValidation } from './models/token-validation';
import { LoginResponse } from './models/login-response';

@Injectable({ providedIn: 'root' })
export class AuthService {

    private authUrl = environment.services.auth;
    private currentUserSubject = new BehaviorSubject<UserInfo | null>(null);
    public currentUser$ = this.currentUserSubject.asObservable();

    constructor(private http: HttpClient) {
        const token = this.getToken();
        if (token) {
            // Decodificar token y establecer usuario actual
            this.setCurrentUserFromToken(token);
        }
    }

    login(credentials: { username: string; password: string }): Observable<LoginResponse> {
        return this.http.post<LoginResponse>(`${this.authUrl}/auth/login`, credentials)
            .pipe(
                tap(response => {
                    if (response.token) {
                        this.setToken(response.token);
                        this.setCurrentUserFromResponse(response);
                    }
                })
            );
    }

    logout(): void {
        localStorage.removeItem('token');
        this.currentUserSubject.next(null);
    }

    getToken(): string | null {
        return localStorage.getItem('token');
    }

    setToken(token: string): void {
        localStorage.setItem('token', token);
    }

    isAuthenticated(): boolean {
        const token = this.getToken();
        return !!token && !this.isTokenExpired(token);
    }

    getCurrentUser(): UserInfo | null {
        return this.currentUserSubject.value;
    }

    private setCurrentUserFromToken(token: string): void {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            let roles: string[] = [];
            
            if (payload.roles) {
                if (typeof payload.roles === 'string') {
                    roles = payload.roles.split(',').map((role: string) => role.trim());
                } else if (Array.isArray(payload.roles)) {
                    roles = payload.roles;
                }
            }
            
            const user: UserInfo = {
                username: payload.sub,
                roles: roles
            };
            this.currentUserSubject.next(user);
        } catch (error) {
            console.error('Error parsing token:', error);
        }
    }

    private setCurrentUserFromResponse(response: LoginResponse): void {
        const user: UserInfo = {
            username: response.username,
            roles: response.roles
        };
        this.currentUserSubject.next(user);
    }

    private isTokenExpired(token: string): boolean {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            return payload.exp * 1000 < Date.now();
        } catch (error) {
            return true;
        }
    }
}
