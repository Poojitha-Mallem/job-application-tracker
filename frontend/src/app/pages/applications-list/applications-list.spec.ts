import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { describe, it, expect, beforeEach } from 'vitest';
import { ApplicationsList } from './applications-list';

describe('ApplicationsList', () => {
    let component: ApplicationsList;
    let fixture: ComponentFixture<ApplicationsList>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [ApplicationsList],
            providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])]
        }).compileComponents();

        fixture = TestBed.createComponent(ApplicationsList);
        component = fixture.componentInstance;
        await fixture.whenStable();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});