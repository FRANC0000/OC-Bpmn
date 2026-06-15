import { Component, output } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatToolbar } from '@angular/material/toolbar';
import { MatIcon } from '@angular/material/icon';
import { MatButton, MatIconButton } from '@angular/material/button';
import { MatMenu, MatMenuContent, MatMenuItem } from '@angular/material/menu';
import { AuthService } from '../services/auth.service';
import { NotificationPanelComponent } from './notification-panel.component';

@Component({
  selector: 'bpm-header',
  standalone: true,
  imports: [MatToolbar, MatIcon, MatButton, MatIconButton, MatMenu, MatMenuContent, MatMenuItem, RouterLink, NotificationPanelComponent],
  template: `
    <mat-toolbar>
      <button mat-icon-button (click)="menuToggle.emit()" class="menu-btn">
        <mat-icon>menu</mat-icon>
      </button>
      <a routerLink="/" class="brand">
        <mat-icon class="brand-icon">assignment</mat-icon>
        <span class="brand-text">BPM Platform</span>
      </a>
      <span class="spacer"></span>
      <bpm-notification-panel />
      <button mat-icon-button [matMenuTriggerFor]="userMenu" class="avatar-btn">
        <mat-icon>account_circle</mat-icon>
      </button>
      <mat-menu #userMenu="matMenu" xPosition="before">
        <div class="user-info">
          <span class="user-name">{{ auth.currentUser()?.displayName }}</span>
          <span class="user-email">{{ auth.currentUser()?.email }}</span>
        </div>
        <button mat-menu-item (click)="toggleTheme()">
          <mat-icon>{{ isDark ? 'light_mode' : 'dark_mode' }}</mat-icon>
          {{ isDark ? 'Modo claro' : 'Modo oscuro' }}
        </button>
        <button mat-menu-item (click)="auth.logout()">
          <mat-icon>logout</mat-icon>
          Cerrar sesión
        </button>
      </mat-menu>
    </mat-toolbar>
  `,
  styles: [`
    mat-toolbar { position: relative; z-index: 10; background: var(--color-surface); border-bottom: 1px solid var(--color-border); }
    .brand { display: flex; align-items: center; gap: 8px; text-decoration: none; color: inherit; }
    .brand-icon { color: var(--color-primary); }
    .brand-text { font-weight: 700; font-size: 16px; }
    .spacer { flex: 1; }
    .menu-btn { display: none; }
    .avatar-btn { margin-left: 4px; }
    .user-info { padding: 8px 16px; border-bottom: 1px solid var(--color-border); }
    .user-name { display: block; font-weight: 600; font-size: 14px; }
    .user-email { display: block; font-size: 12px; color: var(--color-text-secondary); }
    @media (max-width: 767px) { .menu-btn { display: inline-flex; } .brand-text { display: none; } }
  `]
})
export class HeaderComponent {
  isDark = document.documentElement.getAttribute('data-theme') === 'dark';
  menuToggle = output<void>();

  constructor(protected auth: AuthService) {}

  toggleTheme(): void {
    this.isDark = !this.isDark;
    document.documentElement.setAttribute('data-theme', this.isDark ? 'dark' : 'light');
    document.body.classList.toggle('dark-theme', this.isDark);
  }
}
