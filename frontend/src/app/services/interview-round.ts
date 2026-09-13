import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { InterviewRound, InterviewRoundRequest } from '../models/application.model';

@Injectable({ providedIn: 'root' })
export class InterviewRoundService {
    constructor(private http: HttpClient) {}

    getByApplication(applicationId: number): Observable<InterviewRound[]> {
        return this.http.get<InterviewRound[]>(`${environment.apiUrl}/applications/${applicationId}/rounds`);
    }

    create(applicationId: number, request: InterviewRoundRequest): Observable<InterviewRound> {
        return this.http.post<InterviewRound>(`${environment.apiUrl}/applications/${applicationId}/rounds`, request);
    }

    update(roundId: number, request: InterviewRoundRequest): Observable<InterviewRound> {
        return this.http.put<InterviewRound>(`${environment.apiUrl}/rounds/${roundId}`, request);
    }

    delete(roundId: number): Observable<void> {
        return this.http.delete<void>(`${environment.apiUrl}/rounds/${roundId}`);
    }
}