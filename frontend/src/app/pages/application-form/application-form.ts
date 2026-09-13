import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ApplicationService } from '../../services/application.service';
import { ApplicationStatus, InterviewPrepResponse } from '../../models/application.model';

@Component({
    selector: 'app-application-form',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule],
    templateUrl: './application-form.html',
    styleUrl: './application-form.scss'
})
export class ApplicationForm implements OnInit {
    form: FormGroup;
    isEditMode = signal(false);
    applicationId: number | null = null;
    isLoading = signal(false);
    errorMessage = signal<string | null>(null);

    interviewPrep = signal<InterviewPrepResponse | null>(null);
    isGeneratingPrep = signal(false);
    prepErrorMessage = signal<string | null>(null);

    statuses: ApplicationStatus[] = ['APPLIED', 'IN_PROGRESS', 'INTERVIEWING', 'OFFER', 'REJECTED', 'WITHDRAWN'];

    constructor(
        private fb: FormBuilder,
        private applicationService: ApplicationService,
        private route: ActivatedRoute,
        private router: Router
    ) {
        this.form = this.fb.group({
            companyName: ['', [Validators.required]],
            jobTitle: ['', [Validators.required]],
            jobDescription: [''],
            status: ['APPLIED'],
            appliedDate: [''],
            source: [''],
            salaryExpectation: [null]
        });
    }

    ngOnInit(): void {
        const idParam = this.route.snapshot.paramMap.get('id');

        if (idParam) {
            this.isEditMode.set(true);
            this.applicationId = Number(idParam);
            this.loadApplication(this.applicationId);
        }
    }

    loadApplication(id: number): void {
        this.isLoading.set(true);

        this.applicationService.getById(id).subscribe({
            next: (app) => {
                this.form.patchValue({
                    companyName: app.companyName,
                    jobTitle: app.jobTitle,
                    jobDescription: app.jobDescription,
                    status: app.status,
                    appliedDate: app.appliedDate,
                    source: app.source,
                    salaryExpectation: app.salaryExpectation
                });
                this.isLoading.set(false);
            },
            error: () => {
                this.errorMessage.set('Failed to load application.');
                this.isLoading.set(false);
            }
        });
    }

    onSubmit(): void {
        if (this.form.invalid) {
            return;
        }

        this.isLoading.set(true);
        this.errorMessage.set(null);

        const request = this.form.value;

        const operation = this.isEditMode()
            ? this.applicationService.update(this.applicationId!, request)
            : this.applicationService.create(request);

        operation.subscribe({
            next: () => {
                this.isLoading.set(false);
                this.router.navigate(['/applications']);
            },
            error: (err) => {
                this.isLoading.set(false);
                this.errorMessage.set(err.error?.message || 'Failed to save application.');
            }
        });
    }

    generateInterviewPrep(): void {
        if (!this.applicationId) {
            return;
        }

        this.isGeneratingPrep.set(true);
        this.prepErrorMessage.set(null);
        this.interviewPrep.set(null);

        this.applicationService.getInterviewPrep(this.applicationId).subscribe({
            next: (response) => {
                this.interviewPrep.set(response);
                this.isGeneratingPrep.set(false);
            },
            error: () => {
                this.prepErrorMessage.set('Failed to generate interview questions. Please try again.');
                this.isGeneratingPrep.set(false);
            }
        });
    }

    cancel(): void {
        this.router.navigate(['/applications']);
    }
}