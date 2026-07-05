import { Component } from '@angular/core';
import { RouterOutlet, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterModule, CommonModule],
  template: `
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark" *ngIf="authService.isAuthenticated()">
      <div class="container-fluid">
        <a class="navbar-brand" routerLink="/dashboard">
          <strong>BankApp</strong>
        </a>
        <div class="collapse navbar-collapse">
          <ul class="navbar-nav me-auto">
            <li class="nav-item"><a class="nav-link" routerLink="/dashboard" routerLinkActive="active">Dashboard</a></li>
            <li class="nav-item"><a class="nav-link" routerLink="/customers" routerLinkActive="active">Clients</a></li>
            <li class="nav-item"><a class="nav-link" routerLink="/accounts" routerLinkActive="active">Comptes</a></li>
            <li class="nav-item"><a class="nav-link" routerLink="/chatbot" routerLinkActive="active">Chatbot</a></li>
          </ul>
          <button class="btn btn-outline-light btn-sm" (click)="authService.logout()">Déconnexion</button>
        </div>
      </div>
    </nav>
    <router-outlet></router-outlet>
  `
})
export class AppComponent {
  constructor(public authService: AuthService) {}
}
