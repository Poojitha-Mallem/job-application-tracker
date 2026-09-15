import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login';
import { Register } from './pages/register/register';
import { ApplicationsList } from './pages/applications-list/applications-list';
import { ApplicationForm } from './pages/application-form/application-form';
import { authGuard } from './guards/auth-guard';
import { guestGuard } from './guards/guest-guard';
import { KanbanBoard } from './pages/kanban-board/kanban-board';
import { Dashboard } from './pages/dashboard/dashboard';

export const routes: Routes = [
    { path: '', redirectTo: '/login', pathMatch: 'full' },
    { path: 'login', component: LoginComponent, canActivate: [guestGuard] },
    { path: 'register', component: Register, canActivate: [guestGuard] },
    { path: 'applications', component: ApplicationsList, canActivate: [authGuard] },
    { path: 'applications/new', component: ApplicationForm, canActivate: [authGuard] },
    { path: 'applications/:id', component: ApplicationForm, canActivate: [authGuard] },
    { path: 'kanban', component: KanbanBoard, canActivate: [authGuard] },
    { path: 'dashboard', component: Dashboard, canActivate: [authGuard] }
];