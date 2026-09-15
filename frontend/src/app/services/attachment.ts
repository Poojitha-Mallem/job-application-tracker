import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export type FileType = 'RESUME' | 'JOB_DESCRIPTION' | 'COVER_LETTER';

export interface Attachment {
    id: number;
    fileName: string;
    filePath: string;
    fileType: FileType;
    uploadedAt: string;
}

@Injectable({ providedIn: 'root' })
export class AttachmentService {
    constructor(private http: HttpClient) {}

    getByApplication(applicationId: number): Observable<Attachment[]> {
        return this.http.get<Attachment[]>(`${environment.apiUrl}/applications/${applicationId}/attachments`);
    }

    upload(applicationId: number, file: File, fileType: FileType): Observable<Attachment> {
        const formData = new FormData();
        formData.append('file', file);
        formData.append('fileType', fileType);

        return this.http.post<Attachment>(
            `${environment.apiUrl}/applications/${applicationId}/attachments`,
            formData
        );
    }

    delete(attachmentId: number): Observable<void> {
        return this.http.delete<void>(`${environment.apiUrl}/attachments/${attachmentId}`);
    }
}