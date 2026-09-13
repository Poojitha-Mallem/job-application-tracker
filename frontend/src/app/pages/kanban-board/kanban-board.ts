import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { DragDropModule, CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { ApplicationService } from '../../services/application.service';
import { Application, ApplicationStatus } from '../../models/application.model';

interface KanbanColumn {
    status: ApplicationStatus;
    label: string;
    applications: Application[];
}

@Component({
    selector: 'app-kanban-board',
    standalone: true,
    imports: [CommonModule, DragDropModule],
    templateUrl: './kanban-board.html',
    styleUrl: './kanban-board.scss'
})
export class KanbanBoard implements OnInit {
    columns = signal<KanbanColumn[]>([
        { status: 'APPLIED', label: 'Applied', applications: [] },
        { status: 'IN_PROGRESS', label: 'In Progress', applications: [] },
        { status: 'INTERVIEWING', label: 'Interviewing', applications: [] },
        { status: 'OFFER', label: 'Offer', applications: [] },
        { status: 'REJECTED', label: 'Rejected', applications: [] },
        { status: 'WITHDRAWN', label: 'Withdrawn', applications: [] }
    ]);

    isLoading = signal(false);
    errorMessage = signal<string | null>(null);

    constructor(
        private applicationService: ApplicationService,
        protected router: Router
    ) {}

    ngOnInit(): void {
        this.loadAllApplications();
    }

    loadAllApplications(): void {
        this.isLoading.set(true);

        this.applicationService.search(0, 100).subscribe({
            next: (response) => {
                const grouped = this.columns().map(column => ({
                    ...column,
                    applications: response.content.filter(app => app.status === column.status)
                }));
                this.columns.set(grouped);
                this.isLoading.set(false);
            },
            error: () => {
                this.errorMessage.set('Failed to load applications.');
                this.isLoading.set(false);
            }
        });
    }

    getColumnIds(): string[] {
        return this.columns().map(c => c.status);
    }

    onDrop(event: CdkDragDrop<Application[]>): void {
        if (event.previousContainer === event.container) {
            moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
            return;
        }

        const application = event.previousContainer.data[event.previousIndex];
        const newStatus = event.container.id as ApplicationStatus;

        transferArrayItem(
            event.previousContainer.data,
            event.container.data,
            event.previousIndex,
            event.currentIndex
        );

        this.applicationService.updateStatus(application.id, newStatus).subscribe({
            error: () => {
                this.errorMessage.set('Failed to update status. Reverting.');
                this.loadAllApplications();
            }
        });
    }

    viewApplication(id: number): void {
        this.router.navigate(['/applications', id]);
    }
}