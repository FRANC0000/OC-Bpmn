import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { MatIcon } from '@angular/material/icon';

interface BottomNavItem {
  icon: string;
  label: string;
  route: string;
}

@Component({
  selector: 'bpm-bottom-nav',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, MatIcon],
  template: `
    <nav class="bottom-nav">
      @for (item of navItems; track item.route) {
        <a [routerLink]="item.route"
           routerLinkActive="active"
           [routerLinkActiveOptions]="{exact: true}"
           class="nav-item">
          <mat-icon>{{ item.icon }}</mat-icon>
          <span class="label">{{ item.label }}</span>
        </a>
      }
    </nav>
  `,
  styles: [`
    .bottom-nav { display: flex; justify-content: space-around; align-items: center; height: 56px; background: var(--color-surface); border-top: 1px solid var(--color-border); padding: 0 4px; }
    .nav-item { display: flex; flex-direction: column; align-items: center; gap: 2px; padding: 6px 12px; border-radius: 8px; text-decoration: none; color: var(--color-text-secondary); transition: color var(--transition-fast); min-width: 56px; }
    .nav-item.active { color: var(--color-primary); }
    .nav-item.active mat-icon { color: var(--color-primary); }
    .nav-item mat-icon { font-size: 22px; width: 22px; height: 22px; }
    .label { font-size: 10px; font-weight: 500; line-height: 1; }
  `]
})
export class BottomNavComponent {
  navItems: BottomNavItem[] = [
    { icon: 'dashboard', label: 'Dashboard', route: '/tasks' },
    { icon: 'description', label: 'Docs', route: '/documents' },
    { icon: 'account_tree', label: 'Procesos', route: '/processes' },
    { icon: 'draw', label: 'Diseño', route: '/designer' },
    { icon: 'admin_panel_settings', label: 'Admin', route: '/admin' },
  ];
}
