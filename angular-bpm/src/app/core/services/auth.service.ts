import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { LoginRequest, LoginResponse, RegisterRequest, RegisterResponse } from '../models/auth.model';
import { map, tap } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { ApiResponse } from '../models/document.model';

export interface AuthState {
  token: string;
  userId: string;
  email: string;
  displayName: string;
  tenantId: string;
  role: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly api = environment.apiUrl;
  private readonly tokenKey = environment.auth.tokenKey;

  currentUser = signal<AuthState | null>(this.loadUser());

  constructor(private http: HttpClient, private router: Router) {}

  login(req: LoginRequest): Observable<LoginResponse> {
    const tenantId = localStorage.getItem('bpm_tenant_id');
    const body = tenantId ? { ...req, tenantId } : req;
    return this.http.post<ApiResponse<LoginResponse>>(`${this.api}/auth/login`, body).pipe(
      map(r => r.data),
      tap(res => this.saveSession(res))
    );
  }

  register(req: RegisterRequest): Observable<RegisterResponse> {
    return this.http.post<ApiResponse<RegisterResponse>>(`${this.api}/auth/register`, req).pipe(
      map(r => r.data),
      tap(res => {
        localStorage.setItem('bpm_tenant_id', res.tenantId);
      })
    );
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    this.currentUser.set(null);
    this.router.navigate(['/auth/login']);
  }

  getToken(): string | null {
    return this.currentUser()?.token ?? null;
  }

  isAuthenticated(): boolean {
    return this.getToken() !== null;
  }

  isAdmin(): boolean {
    return this.currentUser()?.role === 'ADMIN';
  }

  isSuperAdmin(): boolean {
    return this.currentUser()?.email === 'franco.teran@gmail.com';
  }

  private saveSession(res: LoginResponse): void {
    const tenantId = localStorage.getItem('bpm_tenant_id') || '';
    const state: AuthState = {
      token: res.token,
      userId: res.userId,
      email: res.email,
      displayName: res.displayName,
      tenantId,
      role: res.role,
    };
    localStorage.setItem(this.tokenKey, JSON.stringify(state));
    this.currentUser.set(state);
  }

  private loadUser(): AuthState | null {
    const raw = localStorage.getItem(this.tokenKey);
    if (!raw) return null;
    try {
      return JSON.parse(raw) as AuthState;
    } catch {
      return null;
    }
  }
}
