import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { LoginRequest, LoginResponse, RegisterRequest, RegisterResponse } from '../models/auth.model';
import { tap } from 'rxjs/operators';
import { Observable } from 'rxjs';

export interface AuthState {
  token: string;
  userId: string;
  email: string;
  displayName: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly api = environment.apiUrl;
  private readonly tokenKey = environment.auth.tokenKey;

  currentUser = signal<AuthState | null>(this.loadUser());

  constructor(private http: HttpClient, private router: Router) {}

  login(req: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.api}/auth/login`, req).pipe(
      tap(res => this.saveSession(res))
    );
  }

  register(req: RegisterRequest): Observable<RegisterResponse> {
    return this.http.post<RegisterResponse>(`${this.api}/security/users`, req);
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

  private saveSession(res: LoginResponse): void {
    const state: AuthState = {
      token: res.token,
      userId: res.userId,
      email: res.email,
      displayName: res.displayName,
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
