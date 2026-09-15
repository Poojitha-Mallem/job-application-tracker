import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Stats} from '../../services/stats';
import { StatsResponse } from '../../models/application.model';

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

    statusOrder = ['APPLIED', 'IN_PROGRESS', 'INTERVIEWING', 'OFFER', 'REJECTED', 'WITHDRAWN'];

    constructor(protected router: Router, private statsService: Stats) {}

    ngOnInit(): void {
        this.loadStats();
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