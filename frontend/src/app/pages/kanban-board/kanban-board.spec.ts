import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { describe, it, expect, beforeEach } from 'vitest';
import { KanbanBoard } from './kanban-board';

describe('KanbanBoard', () => {
    let component: KanbanBoard;
    let fixture: ComponentFixture<KanbanBoard>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [KanbanBoard],
            providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])]
        }).compileComponents();

        fixture = TestBed.createComponent(KanbanBoard);
        component = fixture.componentInstance;
        await fixture.whenStable();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});