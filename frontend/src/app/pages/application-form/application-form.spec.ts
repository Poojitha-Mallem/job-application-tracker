import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { describe, it, expect, beforeEach } from 'vitest';
import { ApplicationForm } from './application-form';

describe('ApplicationForm', () => {
    let component: ApplicationForm;
    let fixture: ComponentFixture<ApplicationForm>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [ApplicationForm],
            providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])]
        }).compileComponents();

        fixture = TestBed.createComponent(ApplicationForm);
        component = fixture.componentInstance;
        await fixture.whenStable();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});