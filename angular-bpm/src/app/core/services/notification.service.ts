import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Notification } from '../models/notification.model';
import { Observable, interval, startWith, switchMap } from 'rxjs';

interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

@Injectable({ providedIn: 'root' })
export class NotificationService {
  private readonly api = environment.apiUrl;
  unreadCount = signal(0);
  notifications = signal<Notification[]>([]);

  constructor(private http: HttpClient) {
    interval(30000).pipe(
      startWith(0),
      switchMap(() => this.fetchUnreadCount())
    ).subscribe(count => this.unreadCount.set(count));
  }

  fetchNotifications(): Observable<ApiResponse<Notification[]>> {
    return this.http.get<ApiResponse<Notification[]>>(`${this.api}/notifications`);
  }

  fetchUnreadCount(): Observable<number> {
    return this.http.get<ApiResponse<number>>(`${this.api}/notifications/unread-count`).pipe(
      switchMap(r => [r.data])
    );
  }

  markAsRead(id: string): Observable<ApiResponse<null>> {
    return this.http.post<ApiResponse<null>>(`${this.api}/notifications/${id}/read`, {});
  }

  refresh(): void {
    this.fetchNotifications().subscribe(r => {
      this.notifications.set(r.data);
      this.unreadCount.set(r.data.filter(n => !n.isRead).length);
    });
  }
}
