import { Component, computed } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatIcon } from '@angular/material/icon';
import { MatButton, MatIconButton } from '@angular/material/button';
import { MatMenu, MatMenuContent, MatMenuItem, MatMenuTrigger } from '@angular/material/menu';
import { MatBadge } from '@angular/material/badge';
import { NotificationService } from '../services/notification.service';

@Component({
  selector: 'bpm-notification-panel',
  standalone: true,
  imports: [MatIcon, MatButton, MatIconButton, MatMenu, MatMenuContent, MatMenuTrigger, MatMenuItem, MatBadge, RouterLink],
  template: `
    <button mat-icon-button [matMenuTriggerFor]="notifMenu" class="notif-btn">
      <mat-icon [matBadge]="unreadCount()" matBadgeSize="small" matBadgeColor="warn">notifications</mat-icon>
    </button>
    <mat-menu #notifMenu="matMenu" class="notif-menu" xPosition="before" yPosition="below">
      <div class="notif-header">
        <span class="notif-title">Notificaciones</span>
        <button mat-button (click)="refresh()" class="refresh-btn">Actualizar</button>
      </div>
      @if (notifications().length === 0) {
        <div class="empty">Sin notificaciones</div>
      }
      @for (n of notifications().slice(0, 5); track n.id) {
        <button mat-menu-item class="notif-item" [class.unread]="!n.isRead" (click)="markRead(n.id)">
          <mat-icon>{{ iconFor(n.type) }}</mat-icon>
          <div class="notif-content">
            <span class="notif-text">{{ n.title }}</span>
            <span class="notif-msg">{{ n.message }}</span>
          </div>
        </button>
      }
      @if (notifications().length > 5) {
        <a mat-menu-item routerLink="/notifications" class="see-all">Ver todas</a>
      }
    </mat-menu>
  `,
  styles: [`
    .notif-btn { position: relative; }
    .notif-header { display: flex; justify-content: space-between; align-items: center; padding: 8px 16px; border-bottom: 1px solid var(--color-border); }
    .notif-title { font-weight: 600; font-size: 14px; }
    .refresh-btn { --mdc-text-button-label-text-size: 12px; }
    .empty { padding: 24px 16px; text-align: center; color: var(--color-text-secondary); font-size: 13px; }
    .notif-item { height: auto !important; min-height: 48px; padding: 8px 16px !important; line-height: 1.3 !important; }
    .notif-item.unread { background: color-mix(in srgb, var(--color-primary) 6%, transparent); }
    .notif-content { display: flex; flex-direction: column; }
    .notif-text { font-weight: 600; font-size: 13px; }
    .notif-msg { font-size: 12px; color: var(--color-text-secondary); }
    .see-all { justify-content: center; font-size: 13px; font-weight: 500; color: var(--color-primary); }
  `]
})
export class NotificationPanelComponent {
  notifications = computed(() => this.service.notifications());
  unreadCount = computed(() => this.service.unreadCount());

  constructor(private service: NotificationService) {
    this.service.refresh();
  }

  refresh(): void { this.service.refresh(); }
  markRead(id: string): void { this.service.markAsRead(id).subscribe(() => this.service.refresh()); }

  iconFor(type: string): string {
    if (type.includes('document')) return 'description';
    if (type.includes('process')) return 'account_tree';
    if (type.includes('user')) return 'person';
    return 'notifications';
  }
}
