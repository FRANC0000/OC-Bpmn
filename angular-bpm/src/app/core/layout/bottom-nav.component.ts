import { Component, inject, computed } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { MatIcon } from '@angular/material/icon';
import { AuthService } from '../../core/services/auth.service';

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
      @for (item of navItems(); track item.route) {
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
    .bottom-nav { display: flex; justify-content: space-around; align-items: center; height: 56px; background: var(--color-surface-raised); border-top: 1px solid var(--color-border); padding: 0 4px; }
    .nav-item { display: flex; flex-direction: column; align-items: center; gap: 2px; padding: 6px 12px; border-radius: 8px; text-decoration: none; color: var(--color-on-surface-muted); transition: all var(--transition-fast); min-width: 56px; cursor: pointer; }
    .nav-item:hover { color: var(--color-on-surface); background: var(--color-bg-hover); }
    .nav-item.active { color: var(--color-primary); }
    .nav-item mat-icon { font-size: 22px; width: 22px; height: 22px; }
    .label { font-size: 10px; font-weight: 500; line-height: 1; }
  `]
})
export class BottomNavComponent {
  private auth = inject(AuthService);

  private mainItems: BottomNavItem[] = [
    { icon: 'dashboard', label: 'Dashboard', route: '/tasks' },
    { icon: 'description', label: 'Docs', route: '/documents' },
    { icon: 'account_tree', label: 'Procesos', route: '/processes' },
    { icon: 'draw', label: 'Diseño', route: '/designer' },
  ];

  private adminItems: BottomNavItem[] = [
    { icon: 'business', label: 'Tnts', route: '/admin/tenants' },
    { icon: 'people', label: 'Usrs', route: '/admin/users' },
    { icon: 'admin_panel_settings', label: 'Roles', route: '/admin/roles' },
    { icon: 'lock', label: 'Perms', route: '/admin/permissions' },
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
