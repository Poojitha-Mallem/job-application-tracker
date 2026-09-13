import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environment';

describe('AuthService', () => {
    let service: AuthService;
    let httpMock: HttpTestingController;
    let mockRouter: { navigate: ReturnType<typeof vi.fn> };

    beforeEach(() => {
        mockRouter = { navigate: vi.fn() };

        TestBed.configureTestingModule({
            providers: [
                provideHttpClient(),
                provideHttpClientTesting(),
                { provide: Router, useValue: mockRouter }
            ]
        });

        service = TestBed.inject(AuthService);
        httpMock = TestBed.inject(HttpTestingController);
        localStorage.clear();
    });

    afterEach(() => {
        httpMock.verify();
    });

    it('should store the token and set isAuthenticated to true on successful login', () => {
        service.login({ email: 'test@example.com', password: 'password123' }).subscribe();

        const req = httpMock.expectOne(`${environment.apiUrl}/auth/login`);
        expect(req.request.method).toBe('POST');

        req.flush({ token: 'fake-jwt-token', email: 'test@example.com', role: 'USER' });

        expect(localStorage.getItem('jwt_token')).toBe('fake-jwt-token');
        expect(service.isAuthenticated()).toBe(true);
    });

    it('should clear stored credentials and navigate to login on logout', () => {
        localStorage.setItem('jwt_token', 'some-token');
        localStorage.setItem('user_email', 'test@example.com');

        service.logout();

        expect(localStorage.getItem('jwt_token')).toBeNull();
        expect(service.isAuthenticated()).toBe(false);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
    });

    it('should return null from getToken when no token is stored', () => {
        expect(service.getToken()).toBeNull();
    });
});