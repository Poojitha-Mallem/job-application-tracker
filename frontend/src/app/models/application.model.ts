export type ApplicationStatus =
    | 'APPLIED'
    | 'IN_PROGRESS'
    | 'INTERVIEWING'
    | 'OFFER'
    | 'REJECTED'
    | 'WITHDRAWN';

export type RoundType = 'PHONE_SCREEN' | 'TECHNICAL' | 'SYSTEM_DESIGN' | 'HR' | 'MANAGERIAL';
export type RoundStatus = 'SCHEDULED' | 'COMPLETED' | 'CANCELLED';
export type RoundResult = 'PENDING' | 'PASSED' | 'FAILED';


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

export interface InterviewRound {
    id: number;
    roundType: RoundType;
    scheduledAt: string | null;
    status: RoundStatus;
    feedback: string | null;
    result: RoundResult | null;
    createdAt: string;
    updatedAt: string;
}

export interface InterviewRoundRequest {
    roundType: RoundType;
    scheduledAt?: string;
    status?: RoundStatus;
    feedback?: string;
    result?: RoundResult;
}

export interface StatsResponse {
    totalApplications: number;
    countByStatus: Record<string, number>;
    responseRate: number;
    interviewRate: number;
}