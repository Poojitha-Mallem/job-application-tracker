import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Stats } from '../../services/stats';
import { ReminderService } from '../../services/reminder';
import { StatsResponse, Reminder } from '../../models/application.model';

@Component({
    selector: 'app-dashboard',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './dashboard.html',
    styleUrl: './dashboard.scss'
})
export class Dashboard implements OnInit {
    stats = signal<StatsResponse | null>(null);
    isLoading = signal(false);
    errorMessage = signal<string | null>(null);

    reminders = signal<Reminder[]>([]);

    statusOrder = ['APPLIED', 'IN_PROGRESS', 'INTERVIEWING', 'OFFER', 'REJECTED', 'WITHDRAWN'];

    constructor(
        protected router: Router,
        private statsService: Stats,
        private reminderService: ReminderService
    ) {}

    ngOnInit(): void {
        this.loadStats();
        this.loadReminders();
    }

    loadStats(): void {
        this.isLoading.set(true);
        this.errorMessage.set(null);

        this.statsService.getSummary().subscribe({
            next: (response) => {
                this.stats.set(response);
                this.isLoading.set(false);
            },
            error: () => {
                this.errorMessage.set('Failed to load statistics.');
                this.isLoading.set(false);
            }
        });
    }

    loadReminders(): void {
        this.reminderService.getActive().subscribe({
            next: (reminders) => this.reminders.set(reminders),
            error: () => {}
        });
    }

    dismissReminder(id: number): void {
        this.reminderService.dismiss(id).subscribe({
            next: () => this.reminders.set(this.reminders().filter(r => r.id !== id))
        });
    }

    viewApplication(applicationId: number): void {
        this.router.navigate(['/applications', applicationId]);
    }

    getCount(status: string): number {
        return this.stats()?.countByStatus[status] ?? 0;
    }

    getBarWidth(status: string): number {
        const total = this.stats()?.totalApplications ?? 0;
        if (total === 0) {
            return 0;
        }
        return (this.getCount(status) / total) * 100;
    }
}