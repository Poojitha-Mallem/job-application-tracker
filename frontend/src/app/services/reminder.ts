import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Reminder } from '../models/application.model';

@Injectable({ providedIn: 'root' })
export class ReminderService {
    constructor(private http: HttpClient) {}

    getActive(): Observable<Reminder[]> {
        return this.http.get<Reminder[]>(`${environment.apiUrl}/reminders`);
    }

    dismiss(id: number): Observable<void> {
        return this.http.patch<void>(`${environment.apiUrl}/reminders/${id}/dismiss`, null);
    }
}