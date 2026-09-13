export type ApplicationStatus =
    | 'APPLIED'
    | 'IN_PROGRESS'
    | 'INTERVIEWING'
    | 'OFFER'
    | 'REJECTED'
    | 'WITHDRAWN';

export interface Application {
    id: number;
    companyName: string;
    jobTitle: string;
    jobDescription: string | null;
    status: ApplicationStatus;
    appliedDate: string | null;
    source: string | null;
    salaryExpectation: number | null;
    createdAt: string;
    updatedAt: string;
}

export interface ApplicationRequest {
    companyName: string;
    jobTitle: string;
    jobDescription?: string;
    status?: ApplicationStatus;
    appliedDate?: string;
    source?: string;
    salaryExpectation?: number;
}

export interface PageResponse<T> {
    content: T[];
    totalElements: number;
    totalPages: number;
    number: number;
    size: number;
}

export interface InterviewPrepResponse {
    companyName: string;
    jobTitle: string;
    technicalQuestions: string[];
    behavioralQuestions: string[];
}