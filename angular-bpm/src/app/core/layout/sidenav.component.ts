import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { MatNavList, MatListItem } from '@angular/material/list';
import { MatIcon } from '@angular/material/icon';

interface NavItem {
  icon: string;
  label: string;
  route: string;
}

@Component({
  selector: 'bpm-sidenav',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, MatNavList, MatListItem, MatIcon],
  template: `
    <div class="sidenav-header">
      <mat-icon class="logo-icon">assignment</mat-icon>
      <span class="logo-text">BPM</span>
    </div>
    <mat-nav-list>
      @for (item of navItems; track item.route) {
        <a mat-list-item
           [routerLink]="item.route"
           routerLinkActive="active"
           [routerLinkActiveOptions]="{exact: true}">
          <mat-icon matListItemIcon>{{ item.icon }}</mat-icon>
          <span matListItemTitle>{{ item.label }}</span>
        </a>
      }
    </mat-nav-list>
  `,
  styles: [`
    :host { display: flex; flex-direction: column; height: 100%; background: var(--color-surface); }
    .sidenav-header { display: flex; align-items: center; gap: 10px; padding: 16px 20px; border-bottom: 1px solid var(--color-border); }
    .logo-icon { color: var(--color-primary); font-size: 28px; width: 28px; height: 28px; }
    .logo-text { font-weight: 700; font-size: 18px; color: var(--color-primary); }
    a[mat-list-item] { border-radius: 0 24px 24px 0; margin: 2px 12px 2px 0; }
    a[mat-list-item].active { background: color-mix(in srgb, var(--color-primary) 12%, transparent); }
    a[mat-list-item].active mat-icon { color: var(--color-primary); }
  `]
})
export class SidenavComponent {
  navItems: NavItem[] = [
    { icon: 'dashboard', label: 'Dashboard', route: '/tasks' },
    { icon: 'description', label: 'Documentos', route: '/documents' },
    { icon: 'account_tree', label: 'Procesos', route: '/processes' },
    { icon: 'draw', label: 'Diseñador', route: '/designer' },
    { icon: 'admin_panel_settings', label: 'Admin', route: '/admin' },
  ];
}
