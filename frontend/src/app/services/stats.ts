import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { StatsResponse } from '../models/application.model';

@Injectable({ providedIn: 'root' })
export class Stats {
    constructor(private http: HttpClient) {}

    getSummary(): Observable<StatsResponse> {
        return this.http.get<StatsResponse>(`${environment.apiUrl}/stats/summary`);
    }
}