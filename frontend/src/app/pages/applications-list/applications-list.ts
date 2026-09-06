import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ApplicationService } from '../../services/application.service';
import { AuthService } from '../../services/auth.service';
import { Application, ApplicationStatus } from '../../models/application.model';

@Component({
    selector: 'app-applications-list',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './applications-list.html',
    styleUrl: './applications-list.scss'
})
export class ApplicationsList implements OnInit {
    applications = signal<Application[]>([]);
    totalPages = signal(0);
    totalElements = signal(0);
    currentPage = signal(0);
    isLoading = signal(false);
    errorMessage = signal<string | null>(null);

    companyNameFilter = '';
    statusFilter: ApplicationStatus | '' = '';
    pageSize = 10;

    statuses: ApplicationStatus[] = ['APPLIED', 'IN_PROGRESS', 'INTERVIEWING', 'OFFER', 'REJECTED', 'WITHDRAWN'];

    constructor(
      private applicationService: ApplicationService,
      private authService: AuthService,
      protected router: Router
    ) {}

    ngOnInit(): void {
        this.loadApplications();
    }

    loadApplications(): void {
        this.isLoading.set(true);
        this.errorMessage.set(null);

        this.applicationService
            .search(
                this.currentPage(),
                this.pageSize,
                this.companyNameFilter || undefined,
                this.statusFilter || undefined
            )
            .subscribe({
                next: (response) => {
                    this.applications.set(response.content);
                    this.totalPages.set(response.totalPages);
                    this.totalElements.set(response.totalElements);
                    this.isLoading.set(false);
                },
                error: () => {
                    this.errorMessage.set('Failed to load applications.');
                    this.isLoading.set(false);
                }
            });
    }

    onFilterChange(): void {
        this.currentPage.set(0);
        this.loadApplications();
    }

    goToPage(page: number): void {
        if (page < 0 || page >= this.totalPages()) {
            return;
        }
        this.currentPage.set(page);
        this.loadApplications();
    }

    deleteApplication(id: number, event: Event): void {
        event.stopPropagation();
        if (!confirm('Delete this application?')) {
            return;
        }

        this.applicationService.delete(id).subscribe({
            next: () => this.loadApplications(),
            error: () => this.errorMessage.set('Failed to delete application.')
        });
    }

    viewApplication(id: number): void {
        this.router.navigate(['/applications', id]);
    }

    logout(): void {
        this.authService.logout();
    }
}