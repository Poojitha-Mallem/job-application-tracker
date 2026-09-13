import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { describe, it, expect, beforeEach } from 'vitest';
import { App } from './app';

describe('App', () => {
    let fixture: ComponentFixture<App>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [App],
            providers: [provideRouter([])]
        }).compileComponents();

        fixture = TestBed.createComponent(App);
        await fixture.whenStable();
    });

    it('should create the app', () => {
        expect(fixture.componentInstance).toBeTruthy();
    });
});