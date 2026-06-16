import { Component, OnInit, inject } from '@angular/core';
import { DatePipe } from '@angular/common';
import { MatIcon } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBar } from '@angular/material/snack-bar';
import { NotificationService } from '../../../core/services/notification.service';

@Component({
  selector: 'bpm-notifications-page',
  standalone: true,
  imports: [DatePipe, MatIcon, MatButtonModule, MatCardModule],
  template: `
    <div class="page">
      <div class="page-header">
        <div>
          <h1>Notificaciones</h1>
          <p class="subtitle">Todas tus notificaciones</p>
        </div>
      </div>

      @if (loading) {
        <div class="loading">Cargando notificaciones...</div>
      } @else if (notif.notifications().length === 0) {
        <div class="empty-state">
          <mat-icon>notifications_none</mat-icon>
          <p>Sin notificaciones</p>
        </div>
      }

      @for (n of notif.notifications(); track n.id) {
        <mat-card class="notification-card" [class.unread]="!n.isRead">
          <mat-card-content>
            <div class="notif-header">
              <span class="notif-title">{{ n.title }}</span>
              @if (!n.isRead) {
                <span class="unread-dot"></span>
              }
            </div>
            <p class="notif-message">{{ n.message }}</p>
            <div class="notif-footer">
              <span class="notif-date">{{ n.createdAt | date:'medium' }}</span>
              @if (!n.isRead) {
                <button mat-stroked-button size="small" (click)="markAsRead(n.id)">
                  Marcar como leída
                </button>
              }
            </div>
          </mat-card-content>
        </mat-card>
      }
    </div>
  `,
  styles: [`
    .page { max-width: 720px; }
    .page-header { margin-bottom: 24px; }
    .page-header h1 { font-size: 24px; font-weight: 700; margin-bottom: 4px; }
    .subtitle { color: var(--color-text-secondary); font-size: 14px; }
    .loading { text-align: center; padding: 48px; color: var(--color-text-secondary); }
    .notification-card { margin-bottom: 12px; }
    .notification-card.unread { border-left: 3px solid var(--color-primary); }
    .notif-header { display: flex; align-items: center; gap: 8px; }
    .notif-title { font-weight: 600; font-size: 14px; }
    .unread-dot { width: 8px; height: 8px; border-radius: 50%; background: var(--color-primary); }
    .notif-message { color: var(--color-text-secondary); font-size: 13px; margin: 8px 0; }
    .notif-footer { display: flex; justify-content: space-between; align-items: center; }
    .notif-date { font-size: 12px; color: var(--color-text-disabled); }
    .empty-state { text-align: center; padding: 80px 24px; }
    .empty-state mat-icon { font-size: 56px; width: 56px; height: 56px; color: var(--color-text-disabled); margin-bottom: 16px; }
  `]
})
export class NotificationsPageComponent implements OnInit {
  notif = inject(NotificationService);
  private snackBar = inject(MatSnackBar);

  loading = true;

  ngOnInit() {
    this.notif.fetchNotifications().subscribe({
      next: (r) => {
        this.notif.notifications.set(r.data);
        this.notif.unreadCount.set(r.data.filter(n => !n.isRead).length);
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.snackBar.open('Error al cargar notificaciones', 'Cerrar', { duration: 4000 });
      }
    });
  }

  markAsRead(id: string) {
    this.notif.markAsRead(id).subscribe({
      next: () => {
        this.notif.notifications.update(list =>
          list.map(n => n.id === id ? { ...n, isRead: true } : n)
        );
        this.notif.unreadCount.update(c => Math.max(0, c - 1));
      },
      error: () => this.snackBar.open('Error al marcar notificación', 'Cerrar', { duration: 4000 })
    });
  }
}
