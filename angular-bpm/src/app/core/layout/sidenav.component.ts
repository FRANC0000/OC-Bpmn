import { Component, inject, computed } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { MatIcon } from '@angular/material/icon';
import { AuthService } from '../../core/services/auth.service';

interface NavItem {
  icon: string;
  label: string;
  route: string;
}

@Component({
  selector: 'bpm-sidenav',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, MatIcon],
  template: `
    <div class="sidenav-header">
      <mat-icon class="logo-icon">assignment</mat-icon>
      <span class="logo-text">BPM</span>
    </div>
    <nav class="sidenav-nav">
      @for (item of navItems(); track item.route) {
        <a class="nav-item"
           [routerLink]="item.route"
           routerLinkActive="active"
           [routerLinkActiveOptions]="{exact: true}">
          <mat-icon>{{ item.icon }}</mat-icon>
          <span>{{ item.label }}</span>
        </a>
      }
    </nav>
  `,
  styles: [`
    :host { display: flex; flex-direction: column; height: 100%; background: var(--color-surface-raised); }
    .sidenav-header { display: flex; align-items: center; gap: 10px; padding: 20px 20px 16px; }
    .logo-icon { color: var(--color-primary); font-size: 28px; width: 28px; height: 28px; }
    .logo-text { font-weight: 700; font-size: 18px; color: var(--color-primary); }
    .sidenav-nav { display: flex; flex-direction: column; gap: 2px; padding: 8px 8px; }
  `]
})
export class SidenavComponent {
  private auth = inject(AuthService);

  private mainItems: NavItem[] = [
    { icon: 'dashboard', label: 'Dashboard', route: '/tasks' },
    { icon: 'description', label: 'Documentos', route: '/documents' },
    { icon: 'account_tree', label: 'Procesos', route: '/processes' },
    { icon: 'draw', label: 'Diseñador', route: '/designer' },
  ];

  private adminItems: NavItem[] = [
    { icon: 'business', label: 'Tenants', route: '/admin/tenants' },
    { icon: 'people', label: 'Usuarios', route: '/admin/users' },
    { icon: 'admin_panel_settings', label: 'Roles', route: '/admin/roles' },
    { icon: 'lock', label: 'Permisos', route: '/admin/permissions' },
  ];

  navItems = computed(() => {
    const items = [...this.mainItems];
    if (this.auth.isSuperAdmin()) {
      items.push(...this.adminItems);
    } else if (this.auth.isAdmin()) {
      items.push(...this.adminItems.filter(i => i.route !== '/admin/tenants'));
    }
    return items;
  });
}
