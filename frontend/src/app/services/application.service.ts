import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Application, ApplicationRequest, ApplicationStatus, PageResponse } from '../models/application.model';

@Injectable({ providedIn: 'root' })
export class ApplicationService {
    private baseUrl = `${environment.apiUrl}/applications`;

    constructor(private http: HttpClient) {}

    search(
        page: number = 0,
        size: number = 10,
        companyName?: string,
        status?: ApplicationStatus,
        sortBy: string = 'createdAt',
        direction: string = 'desc'
    ): Observable<PageResponse<Application>> {
        let params = new HttpParams()
            .set('page', page)
            .set('size', size)
            .set('sortBy', sortBy)
            .set('direction', direction);

        if (companyName) {
            params = params.set('companyName', companyName);
        }
        if (status) {
            params = params.set('status', status);
        }

        return this.http.get<PageResponse<Application>>(this.baseUrl, { params });
    }

    getById(id: number): Observable<Application> {
        return this.http.get<Application>(`${this.baseUrl}/${id}`);
    }

    create(request: ApplicationRequest): Observable<Application> {
        return this.http.post<Application>(this.baseUrl, request);
    }

    update(id: number, request: ApplicationRequest): Observable<Application> {
        return this.http.put<Application>(`${this.baseUrl}/${id}`, request);
    }

    updateStatus(id: number, status: ApplicationStatus): Observable<Application> {
        const params = new HttpParams().set('status', status);
        return this.http.patch<Application>(`${this.baseUrl}/${id}/status`, null, { params });
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }
}