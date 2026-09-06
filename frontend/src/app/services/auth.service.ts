import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { AuthResponse, LoginRequest, RegisterRequest } from '../models/auth.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
    private tokenKey = 'jwt_token';
    private emailKey = 'user_email';

    isAuthenticated = signal<boolean>(this.hasToken());

    constructor(private http: HttpClient, private router: Router) {}

    register(request: RegisterRequest): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/register`, request)
            .pipe(tap(response => this.handleAuthSuccess(response)));
    }

    login(request: LoginRequest): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/login`, request)
            .pipe(tap(response => this.handleAuthSuccess(response)));
    }

    logout(): void {
        localStorage.removeItem(this.tokenKey);
        localStorage.removeItem(this.emailKey);
        this.isAuthenticated.set(false);
        this.router.navigate(['/login']);
    }

    getToken(): string | null {
        return localStorage.getItem(this.tokenKey);
    }

    getEmail(): string | null {
        return localStorage.getItem(this.emailKey);
    }

    private hasToken(): boolean {
        return !!localStorage.getItem(this.tokenKey);
    }

    private handleAuthSuccess(response: AuthResponse): void {
        localStorage.setItem(this.tokenKey, response.token);
        localStorage.setItem(this.emailKey, response.email);
        this.isAuthenticated.set(true);
    }
}